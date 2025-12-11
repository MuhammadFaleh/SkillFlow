package org.example.skillflow.Service;


import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.CompanyAdminDTOIn;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.CompanyAdmin;
import org.example.skillflow.Repository.CompanyAdminRepository;
import org.example.skillflow.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyAdminService {

    private final CompanyAdminRepository companyAdminRepository;
    private final CompanyRepository companyRepository;

    public List<CompanyAdmin> getCompanyAdmin(){
        return companyAdminRepository.findAll();
    }

    public void addCompanyAdmin(CompanyAdminDTOIn companyAdminDTOIn){
        Company company = companyRepository.findCompanyById(companyAdminDTOIn.getCompanyID());
        if (company == null){
            throw new APIException("company doesn't exist");
        }

        CompanyAdmin companyAdmin = convertToEntity(companyAdminDTOIn);
        companyAdmin.setCompany(company);
        companyAdminRepository.save(companyAdmin);
    }

    public void updateCompanyAdmin(Integer companyAdmin , CompanyAdminDTOIn companyAdminDTOIn){
        CompanyAdmin companyAdmin1 = companyAdminRepository.findCompanyAdminById(companyAdmin);
        Company company = companyRepository.findCompanyById(companyAdminDTOIn.getCompanyID());
        if (companyAdmin1 == null){
            throw new APIException("company admin doesn't exist");
        }
        if (company == null){
            throw new APIException("company does't exist");
        }
        companyAdmin1.setPassword(companyAdminDTOIn.getPassword());
        companyAdmin1.setUsername(companyAdminDTOIn.getUsername());
        companyAdmin1.setEmail(companyAdminDTOIn.getEmail());
        companyAdmin1.setCompany(company);
        companyAdminRepository.save(companyAdmin1);
    }

    public void deleteAdminCompany(Integer companyAdmin){
        CompanyAdmin companyAdmin1 = companyAdminRepository.findCompanyAdminById(companyAdmin);
        if (companyAdmin1 == null){
            throw new APIException("company admin doesn't exist");
        }
        companyAdminRepository.delete(companyAdmin1);
    }
    public CompanyAdmin convertToEntity(CompanyAdminDTOIn companyAdminDTOIn){
        return new CompanyAdmin(companyAdminDTOIn.getCompanyAdminId() , companyAdminDTOIn.getUsername(), companyAdminDTOIn.getEmail(), companyAdminDTOIn.getPassword() , null , null);
    }


}
