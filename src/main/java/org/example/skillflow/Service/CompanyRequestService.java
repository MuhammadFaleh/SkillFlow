package org.example.skillflow.Service;


import lombok.AllArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.Model.Company_Request;
import org.example.skillflow.Repository.Company_RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyRequestService {

    private final CompanyRequestRepository companyRequestRepository;

    public List<CompanyRequest> getAllCompanyRequests(){
        return companyRequestRepository.findAll();
    }

    public void addCompanyRequest(CompanyRequestDTOIn companyRequest){
        CompanyRequest latestRequest = companyRequestRepository.findTheLatestRequest(companyRequest.getRecordNumber());
        if (latestRequest != null){
            if ("pending".equalsIgnoreCase(latestRequest.getStatus())){
            throw new APIException("you have previous order is status pending");
            }
            if ("approved".equalsIgnoreCase(latestRequest.getStatus())){
            throw new APIException("you have previous order is status approved");
            }
        }
        CompanyRequest companyRequest1 = convertToEntity(companyRequest);
        companyRequest1.setRequestDate(LocalDate.now());
        companyRequest1.setStatus("pending");
        companyRequestRepository.save(companyRequest1);
    }

    public void updateCompanyRequest(Integer id , CompanyRequestDTOIn companyRequestDTOIn){
        CompanyRequest latestRequest = companyRequestRepository.findTheLatestRequest(companyRequestDTOIn.getRecordNumber());
        if (latestRequest != null) {
            if ("approved".equalsIgnoreCase(latestRequest.getStatus())) {
                throw new APIException("you have previous order is status approved");
            }
            if ("rejected".equalsIgnoreCase(latestRequest.getStatus())) {
                throw new APIException("this request is rejected. Please submit a new request");
            }
        }

       CompanyRequest checked = companyRequestRepository.findCompanyRequestById(id);
       if (checked == null){
           throw new APIException("Company Request Not found");
       }
       if (!"pending".equalsIgnoreCase(checked.getStatus())){
           throw new APIException("the request is not pending");
       }
//       convert to entity
       checked.setCompanyName(companyRequestDTOIn.getCompanyName());
       checked.setCountry(companyRequestDTOIn.getCountry());
       checked.setFullName(companyRequestDTOIn.getFullName());
       checked.setIndustry(companyRequestDTOIn.getIndustry());
       checked.setRecordNumber(companyRequestDTOIn.getRecordNumber());

       companyRequestRepository.save(checked);
    }

    public void deleteCompanyRequest(Integer id){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(id);
        if (companyRequest == null ){
            throw new APIException("company request not found");
        }
        if (!companyRequest.getStatus().equalsIgnoreCase("pending")){
            throw new APIException("only pending requests can be deleted");
        }
        companyRequestRepository.delete(companyRequest);
    }

    public CompanyRequest checkStatusOrderByRecordNumber(String recordNumber){
        return companyRequestRepository.findTheLatestRequest(recordNumber);
    }

    public CompanyRequest convertToEntity(CompanyRequestDTOIn companyRequestDTOIn){
        return new CompanyRequest(companyRequestDTOIn.getCompanyRequestId() , companyRequestDTOIn.getFullName() , companyRequestDTOIn.getEmail() , companyRequestDTOIn.getCompanyName() , companyRequestDTOIn.getRecordNumber() , companyRequestDTOIn.getCountry() , companyRequestDTOIn.getIndustry() , null , null , null , null);
    }
}
