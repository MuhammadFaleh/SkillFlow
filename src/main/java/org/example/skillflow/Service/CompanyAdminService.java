package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.CompanyAdminDTOIn;
import org.example.skillflow.DTO.Out.CompanyAdminDTOOut;
import org.example.skillflow.DTO.Out.NewSkillRequestDTOOut;
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
    private final ProjectRepository projectRepository;
    private final EmailService emailService;

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

//        for testing later
//        emailService.sendEmail(employee.getEmail(), "New skill Request approved", "Your request with id: "+requestId+ ", has been approved, skill: "
//                + skill.getName()+", has been added to the company skills");
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

        //for testing later
        //emailService.sendEmail(employee.getEmail(), "New skill Request Rejected", "Your request with id: "+requestId+ ", has been rejected");
    }

    public List<NewSkillRequestDTOOut> getNewSkillRequestsForAdmin(Integer adminId) {
        CompanyAdmin admin = companyAdminRepository.findCompanyAdminById(adminId);
        if (admin == null) {
            throw new APIException("company admin not found with id: " + adminId);
        }

        List<NewSkillRequest> requests = newSkillRequestRepository.findByCompanyAdminId(adminId);
        List<NewSkillRequestDTOOut> newSkillRequestDTOOuts = new ArrayList<>();

        for (NewSkillRequest r : requests) {
            newSkillRequestDTOOuts.add(new NewSkillRequestDTOOut(r.getId(), r.getName(), r.getDescription(), r.getStatus(), r.getStart_date(),r.getEnd_date()));
        }
        return newSkillRequestDTOOuts;
    }

    public List<NewSkillRequestDTOOut> getPendingNewSkillRequestsForAdmin(Integer adminId) {
        CompanyAdmin admin = companyAdminRepository.findCompanyAdminById(adminId);
        if (admin == null) {

            throw new APIException("company admin not found with id: " + adminId);
        }

        List<NewSkillRequest> requests = newSkillRequestRepository.findPendingByCompanyAdminId(adminId);
        List<NewSkillRequestDTOOut> newSkillRequestDTOOuts = new ArrayList<>();

        for (NewSkillRequest r : requests) {
            newSkillRequestDTOOuts.add(new NewSkillRequestDTOOut(r.getId(), r.getName(), r.getDescription(), r.getStatus(), r.getStart_date(),r.getEnd_date()));
        }
        return newSkillRequestDTOOuts;
    }

    public void approveProject(Integer adminId, Integer projectId, Integer companyId) {

        CompanyAdmin admin = companyAdminRepository.findCompanyAdminById(adminId);
        if (admin == null) throw new APIException("company admin not found with id: " + adminId);

        Company company = companyRepository.findCompanyById(companyId);
        if (company == null) throw new APIException("company not found with id: " + companyId);

        if (!admin.getCompany().getId().equals(companyId)) {
            throw new APIException("admin is not in this company");
        }

        Project project = projectRepository.findProjectById(projectId);
        if (project == null) throw new APIException("project not found with id: " + projectId);

        if (!project.getCompany().getId().equals(companyId)) {
            throw new APIException("project is not in this company");
        }

        if (!project.getStatus().equalsIgnoreCase("pending")) {
            throw new APIException("only pending projects can be approved, current status: " + project.getStatus());
        }

        project.setStatus("approved");
        projectRepository.save(project);
    }


    public void startProject(Integer adminId, Integer projectId, Integer companyId) {

        CompanyAdmin admin = companyAdminRepository.findCompanyAdminById(adminId);
        if (admin == null) throw new APIException("company admin not found with id: " + adminId);

        Company company = companyRepository.findCompanyById(companyId);
        if (company == null) throw new APIException("company not found with id: " + companyId);

        if (!admin.getCompany().getId().equals(companyId)) {
            throw new APIException("admin is not in this company");
        }

        Project project = projectRepository.findProjectById(projectId);
        if (project == null) throw new APIException("project not found with id: " + projectId);

        if (!project.getCompany().getId().equals(companyId)) {
            throw new APIException("project is not in this company");
        }

        if (!project.getStatus().equalsIgnoreCase("approved")) {
            throw new APIException("only approved projects can be started, current status: " + project.getStatus());
        }

        project.setStatus("in_progress");

        if (project.getStart_date() == null) {
            project.setStart_date(LocalDateTime.now());
        }

        projectRepository.save(project);
    }


    public void rejectProject(Integer adminId, Integer projectId, Integer companyId) {

        CompanyAdmin admin = companyAdminRepository.findCompanyAdminById(adminId);
        if (admin == null) throw new APIException("company admin not found with id: " + adminId);

        Company company = companyRepository.findCompanyById(companyId);
        if (company == null) throw new APIException("company not found with id: " + companyId);

        if (!admin.getCompany().getId().equals(companyId)) {
            throw new APIException("admin is not in this company");
        }

        Project project = projectRepository.findProjectById(projectId);
        if (project == null) throw new APIException("project not found with id: " + projectId);

        if (!project.getCompany().getId().equals(companyId)) {
            throw new APIException("project is not in this company");
        }

        if (!project.getStatus().equalsIgnoreCase("pending")) {
            throw new APIException("only pending projects can be rejected, current status: " + project.getStatus());
        }

        project.setStatus("rejected");
        project.setEnd_date(LocalDateTime.now());
        projectRepository.save(project);

    }

    public CompanyAdmin convertToEntity(CompanyAdminDTOIn dtoIn) {
        return new CompanyAdmin(dtoIn.getCompanyAdmin_id(), dtoIn.getUsername(), dtoIn.getPassword(), null, null);
    }

    public CompanyAdminDTOOut convertToDTO(CompanyAdmin admin) {
        return new CompanyAdminDTOOut(admin.getId(), admin.getUsername(), admin.getCompany().getId());
    }
}
