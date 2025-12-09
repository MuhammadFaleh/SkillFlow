package org.example.skillflow.Service;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Repository.CompanyRequestRepository;
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

    public void addCompanyRequest(@Valid CompanyRequest companyRequest){
        companyRequest.setRequestDate(LocalDate.now());
        companyRequest.setEndDate(LocalDate.now().plusMonths(1));
        companyRequest.setStatus("pending");
        companyRequestRepository.save(companyRequest);
    }

    public void updateCompanyRequest(Integer id , @Valid CompanyRequest companyRequest){
       CompanyRequest companyRequest1 = companyRequestRepository.findCompanyRequestById(id);
       if (companyRequest1 == null){
           throw new APIException("Company Request Not found");
       }
       if ("accepted".equalsIgnoreCase(companyRequest.getStatus())){
           throw new APIException("the request is accepted");
       }
       companyRequest1.setCompanyName(companyRequest.getCompanyName());
       companyRequest1.setCounty(companyRequest.getCounty());
       companyRequest1.setFullName(companyRequest.getFullName());
       companyRequest1.setIndustry(companyRequest.getIndustry());
       companyRequest1.setRecordNumber(companyRequest.getRecordNumber());
       companyRequest1.setRequestDate(LocalDate.now());
       companyRequest1.setEndDate(LocalDate.now().plusMonths(1));
       companyRequestRepository.save(companyRequest1);
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
}
