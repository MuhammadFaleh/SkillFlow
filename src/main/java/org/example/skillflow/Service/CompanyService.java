package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.CompanyDTOIn;
import org.example.skillflow.DTO.In.CreateUserCompanyDTO;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Repository.CompanyRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyRequestRepository companyRequestRepository;

    public List<Company> getCompany(){
        return companyRepository.findAll();
    }

    public void addCompany(CompanyDTOIn company){
        CompanyRequest companyRequest = companyRequestRepository.findTheLatestRequest(company.getRecord_number());
        if(companyRequest == null || !companyRequest.getStatus().equalsIgnoreCase("approved")){
            throw new APIException("request is not approved");
        }

        Company company1 = convertToEntity(company);
        company1.setCountry(companyRequest.getCountry());
        company1.setIndustry(companyRequest.getIndustry());
        company1.setCreated_at(LocalDate.now());
        companyRepository.save(company1);
    }

    public void updateCompany(Integer companyId , CompanyDTOIn company){
        Company oldCompany = companyRepository.findCompanyById(companyId);
        if (oldCompany == null){
            throw new APIException("company not found");
        }

        oldCompany.setCountry(company.getCountry());
        oldCompany.setEmail(company.getEmail());
        oldCompany.setIndustry(company.getIndustry());
        oldCompany.setPassword(company.getPassword());
        oldCompany.setUsername(company.getUsername());
        oldCompany.setName(company.getName());
        companyRepository.save(oldCompany);
    }

    public void deleteCompany(Integer companyId){
        Company company = companyRepository.findCompanyById(companyId);
        if (company == null){
            throw new APIException("company not found");
        }
        companyRepository.delete(company);
    }

    public void createUserCompany(String recordNumber , CreateUserCompanyDTO createUserCompanyDTO){
        CompanyRequest companyRequest = companyRequestRepository.findTheLatestRequest(recordNumber);
        if (companyRequest == null){
            throw new APIException("no request found for this record number");
        }
        if (!"approved".equalsIgnoreCase(companyRequest.getStatus())){
            throw new APIException("request not approved");
        }
        companyRepository.save(handleDTOCreateUser(companyRequest ,createUserCompanyDTO));
    }

    public Company handleDTOCreateUser(CompanyRequest companyRequest , CreateUserCompanyDTO createUserCompanyDTO){
<<<<<<< HEAD
        return new Company(createUserCompanyDTO.getCompanyId(),companyRequest.getCompanyName() , createUserCompanyDTO.getUsername() , createUserCompanyDTO.getEmail() , createUserCompanyDTO.getPassword() , companyRequest.getRecordNumber() , companyRequest.getCountry() , companyRequest.getIndustry() , LocalDate.now() , null , null , null,null,null);
    }
    public Company convertToEntity(CompanyDTOIn companyDTOIn){
        return new Company(companyDTOIn.getCompanyId() , companyDTOIn.getName() , companyDTOIn.getUsername() , companyDTOIn.getEmail() , companyDTOIn.getPassword() , companyDTOIn.getRecord_number() , companyDTOIn.getCountry() , companyDTOIn.getIndustry() , null , null , null , null,null,null);
=======
        return new Company(createUserCompanyDTO.getCompanyId(),companyRequest.getCompanyName() , createUserCompanyDTO.getUsername() , createUserCompanyDTO.getEmail() , createUserCompanyDTO.getPassword() , companyRequest.getRecordNumber() , companyRequest.getCountry() , companyRequest.getIndustry() , LocalDate.now() , null , null , null , null , null , null);
    }
    public Company convertToEntity(CompanyDTOIn companyDTOIn){
        return new Company(companyDTOIn.getCompanyId() , companyDTOIn.getName() , companyDTOIn.getUsername() , companyDTOIn.getEmail() , companyDTOIn.getPassword() , companyDTOIn.getRecord_number() , companyDTOIn.getCountry() , companyDTOIn.getIndustry(), LocalDate.now(), null , null , null , null , null , null);
>>>>>>> Abdulmajed
    }
}