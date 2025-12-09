package org.example.skillflow.Service;

import lombok.AllArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.Model.Admin;
import org.example.skillflow.Model.Company_Request;
import org.example.skillflow.Repository.AdminRepository;
import org.example.skillflow.Repository.Company_RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final Company_RequestRepository company_RequestRepository;

    public List<Admin> getAllAdmin(){
        return adminRepository.findAll();
    }

    public void addAdmin(Admin admin){
        adminRepository.save(admin);
    }

    public void updateAdmin(Integer adminId , Admin admin){
        Admin oldAdmin = adminRepository.findAdminById(adminId);
        if (oldAdmin == null){
            throw new APIException("Admin not found");
        }
        oldAdmin.setEmail(admin.getEmail());
        oldAdmin.setPassword(admin.getPassword());
        oldAdmin.setUsername(admin.getUsername());
        adminRepository.save(oldAdmin);
    }

    public void deleteAdmin(Integer id){
        Admin admin = adminRepository.findAdminById(id);
        if (admin == null){
            throw new APIException("admin not found");
        }
        adminRepository.delete(admin);
    }

    public List<Company_Request> getCompanyRequestByStatus(){
        return company_RequestRepository.getCompany_RequestByStatus("pending");
    }


    public void applyRequestCompany(Integer requestCompanyId){
        Company_Request companyRequest = company_RequestRepository.findCompany_RequestById(requestCompanyId);

        if (companyRequest == null){
            throw new APIException("request not found");
        }
        if (!"pending".equalsIgnoreCase(companyRequest.getStatus())){
            throw new APIException("the request is not pending");
        }


        companyRequest.setStatus("accepted");

        // todo - send email and show user how to add email and username and password

        // add DTO for this because user entered just email and username and password

    }
}
