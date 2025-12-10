package org.example.skillflow.Service;

import lombok.AllArgsConstructor;
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

    public List<Admin> getAllAdmin(){
        return adminRepository.findAll();
    }

    public void addAdmin(AdminDTOIn admin){
        Admin admin1 = convertToEntity(admin);
        adminRepository.save(admin1);
    }

    public void updateAdmin(Integer adminId , AdminDTOIn admin){
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

    public List<CompanyRequest> getCompanyRequestByStatus(){
        return companyRequestRepository.getCompanyRequestByStatus("pending");
    }


    public void applyRequestCompany(Integer adminId , Integer requestCompanyId){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(requestCompanyId);
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null){
            throw new APIException("Admin not found");
        }
        if (companyRequest == null){
            throw new APIException("request not found");
        }
        if (!"pending".equalsIgnoreCase(companyRequest.getStatus())){
            throw new APIException("the request is not pending");
        }
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
    }

}