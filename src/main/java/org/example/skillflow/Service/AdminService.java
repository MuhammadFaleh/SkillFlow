package org.example.skillflow.Service;

import lombok.AllArgsConstructor;
<<<<<<< HEAD
import org.example.skillflow.API.APIException;
import org.example.skillflow.Model.Admin;
import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Repository.AdminRepository;
import org.example.skillflow.Repository.Company_RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final Company_RequestRepository company_RequestRepository;
=======
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.AdminDTOIn;
import org.example.skillflow.Model.Admin;
import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Repository.AdminRepository;
import org.example.skillflow.Repository.CompanyRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final CompanyRequestRepository companyRequestRepository;
>>>>>>> test

    public List<Admin> getAllAdmin(){
        return adminRepository.findAll();
    }

<<<<<<< HEAD
    public void addAdmin(Admin admin){
        adminRepository.save(admin);
    }

    public void updateAdmin(Integer adminId , Admin admin){
=======
    public void addAdmin(AdminDTOIn admin){
        Admin admin1 = convertToEntity(admin);
        adminRepository.save(admin1);
    }

    public void updateAdmin(Integer adminId , AdminDTOIn admin){
>>>>>>> test
        Admin oldAdmin = adminRepository.findAdminById(adminId);
        if (oldAdmin == null){
            throw new APIException("Admin not found");
        }
<<<<<<< HEAD
=======

>>>>>>> test
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

    public List<CompanyRequest> getCompanyRequestByStatus(){
<<<<<<< HEAD
        return company_RequestRepository.getCompany_RequestByStatus("pending");
    }


    public void applyRequestCompany(Integer requestCompanyId){
        CompanyRequest companyRequest = company_RequestRepository.findCompany_RequestById(requestCompanyId);

=======
        return companyRequestRepository.getCompanyRequestByStatus("pending");
    }


    public void applyRequestCompany(Integer adminId , Integer requestCompanyId){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(requestCompanyId);
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null){
            throw new APIException("Admin not found");
        }
>>>>>>> test
        if (companyRequest == null){
            throw new APIException("request not found");
        }
        if (!"pending".equalsIgnoreCase(companyRequest.getStatus())){
            throw new APIException("the request is not pending");
        }
<<<<<<< HEAD


        companyRequest.setStatus("accepted");

        // todo - send email and show user how to add email and username and password

        // add DTO for this because user entered just email and username and password

=======
        companyRequest.setStatus("approved");
        companyRequest.setCheckedByAdmin(adminId);
        companyRequest.setEndDate(LocalDate.now());
        companyRequestRepository.save(companyRequest);
    }

    public void rejectRequestCompany(Integer adminId , Integer companyRequestId){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(companyRequestId);
        Admin admin = adminRepository.findAdminById(adminId);
        if (companyRequest == null){
            throw new APIException("request not found");
        }
        if (admin == null){
            throw new APIException("admin not found");
        }
        if (!"pending".equalsIgnoreCase(companyRequest.getStatus())){
            throw new APIException("Only pending request can be rejected");
        }
        companyRequest.setStatus("rejected");
        companyRequest.setCheckedByAdmin(adminId);
        companyRequest.setEndDate(LocalDate.now());
        companyRequestRepository.save(companyRequest);
    }
    public Admin convertToEntity(AdminDTOIn adminDTOIn){
        return new Admin(adminDTOIn.getAdminId() , adminDTOIn.getUsername() , adminDTOIn.getEmail() , adminDTOIn.getPassword());
>>>>>>> test
    }
}
