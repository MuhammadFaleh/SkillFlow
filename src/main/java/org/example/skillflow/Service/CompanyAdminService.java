package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.CompanyAdminDTOIn;
import org.example.skillflow.DTO.Out.CompanyAdminDTOOut;
import org.example.skillflow.Model.*;
import org.example.skillflow.Repository.*;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Repository.NewSkillRequestRepository;
import org.example.skillflow.Repository.SkillsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyAdminService {

    private final CompanyAdminRepository companyAdminRepository;
    private final CompanyRepository companyRepository;
    private final NewSkillRequestRepository newSkillRequestRepository;
    private final SkillsRepository skillsRepository;

    public List<CompanyAdminDTOOut> getCompanyAdmins() {
        List<CompanyAdminDTOOut> companyAdminDTOOuts = new ArrayList<>();

        for (CompanyAdmin admin : companyAdminRepository.findAll()) {
            companyAdminDTOOuts.add(convertToDTO(admin));
        }

        return companyAdminDTOOuts;
    }

    public void createCompanyAdmin(CompanyAdminDTOIn companyAdminDTOIn) {
        Company company = companyRepository.findCompanyById(companyAdminDTOIn.getCompany_id());

        if (company == null) {
            throw new APIException("company not found with id: " + companyAdminDTOIn.getCompany_id());
        }

        CompanyAdmin companyAdmin = convertToEntity(companyAdminDTOIn);
        companyAdmin.setCompany(company);

        companyAdminRepository.save(companyAdmin);
    }

    public void updateCompanyAdmin(Integer id, CompanyAdminDTOIn companyAdminDTOIn) {
        CompanyAdmin companyAdmin = companyAdminRepository.findCompanyAdminById(id);
        if (companyAdmin == null) {
            throw new APIException("company admin not found with id: " + id);
        }

        Company company = companyRepository.findCompanyById(companyAdminDTOIn.getCompany_id());
        if (company == null) {
            throw new APIException("company not found with id: " + companyAdminDTOIn.getCompany_id());
        }

        companyAdmin.setUsername(companyAdminDTOIn.getUsername());
        companyAdmin.setPassword(companyAdminDTOIn.getPassword());
        companyAdmin.setCompany(company);

        companyAdminRepository.save(companyAdmin);
    }

    public void deleteCompanyAdmin(Integer id, Integer companyId) {
        CompanyAdmin companyAdmin = companyAdminRepository.findCompanyAdminById(id);
        if (companyAdmin == null) {
            throw new APIException("company admin not found with id: " + id);
        }

        if (!companyAdmin.getCompany().getId().equals(companyId)) {
            throw new APIException("company admin does not match company with id: " + companyId);
        }

        companyAdminRepository.delete(companyAdmin);
    }

    public void approveNewSkillRequest(Integer adminId, Integer requestId) {
        CompanyAdmin companyAdmin = companyAdminRepository.findCompanyAdminById(adminId);
        if (companyAdmin == null) {
            throw new APIException("company admin not found with id: " + adminId);
        }

        NewSkillRequest request = newSkillRequestRepository.findNewSkillRequestById(requestId);
        if (request == null) {
            throw new APIException("new skill request not found with id: " + requestId);
        }

        if (!request.getStatus().equalsIgnoreCase("pending")) {
            throw new APIException("only pending requests can be approved, current status: "+ request.getStatus());
        }

        Employee employee = request.getEmployee();
        if (employee == null || employee.getCompany() == null) {
            throw new APIException("request employee or employee's company do not exist");
        }

        if (!companyAdmin.getCompany().getId().equals(employee.getCompany().getId())) {
            throw new APIException("companyAdmin and employee do not have the same company");
        }

        Skills skill = new Skills();
        skill.setName(request.getName());
        skill.setDescription(request.getDescription());
        skill.setCompany(employee.getCompany());

        skillsRepository.save(skill);


        request.setCompanyAdmin(companyAdmin);
        request.setStatus("approved");
        request.setEnd_date(LocalDateTime.now());
        newSkillRequestRepository.save(request);
    }
    public void rejectNewSkillRequest(Integer adminId, Integer requestId) {
        CompanyAdmin companyAdmin = companyAdminRepository.findCompanyAdminById(adminId);
        if (companyAdmin == null) {
            throw new APIException("company admin not found with id: " + adminId);
        }

        NewSkillRequest request = newSkillRequestRepository.findNewSkillRequestById(requestId);
        if (request == null) {
            throw new APIException("new skill request not found with id: " + requestId);
        }

        if (!request.getStatus().equalsIgnoreCase("pending")) {
            throw new APIException("only pending requests can be rejected, current status: " + request.getStatus());
        }

        Employee employee = request.getEmployee();
        if (employee == null || employee.getCompany() == null) {
            throw new APIException("request employee or employee's company do not exist");
        }

        if (!companyAdmin.getCompany().getId().equals(employee.getCompany().getId())) {
            throw new APIException("companyAdmin and employee do not have the same company");
        }

        request.setCompanyAdmin(companyAdmin);
        request.setStatus("rejected");
        request.setEnd_date(LocalDateTime.now());

        newSkillRequestRepository.save(request);
    }


    public CompanyAdmin convertToEntity(CompanyAdminDTOIn dtoIn) {
        return new CompanyAdmin(dtoIn.getCompanyAdmin_id(), dtoIn.getUsername(), dtoIn.getPassword(), null, null);
    }

    public CompanyAdminDTOOut convertToDTO(CompanyAdmin admin) {
        return new CompanyAdminDTOOut(admin.getId(), admin.getUsername(), admin.getCompany().getId());
    }
}
