package org.example.skillflow.Service;

import lombok.AllArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.Company_Request;
import org.example.skillflow.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> getCompany(){
        return companyRepository.findAll();
    }

    public void addCompany(Company company){
        company.setCreated_at(LocalDate.now());
        companyRepository.save(company);
    }

    public void updateCompany(Integer companyId , Company company){
        Company oldCompany = companyRepository.findCompanyById(companyId);
        if (oldCompany == null){
            throw new APIException("company not found");
        }
        oldCompany.setCounty(company.getCounty());
        oldCompany.setCreated_at(LocalDate.now());
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
}
