package org.example.skillflow.Service;


import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.CompanyAdminDTOIn;
import org.example.skillflow.DTO.In.RequestTrainingDTOIn;
import org.example.skillflow.DTO.Out.NewSkillRequestDTOOut;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.CompanyAdmin;
import org.example.skillflow.Repository.CompanyAdminRepository;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.DTO.Out.CompanyAdminDTOOut;
import org.example.skillflow.Model.*;
import org.example.skillflow.Repository.*;
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
    private final RequestTrainingRepository requestTrainingRepository;
    private final TrainingRepository trainingRepository;
    private final ProjectRepository projectRepository;
    private final EmailService emailService;
    private final AiService aiService;
    private final EmployeeRepository employeeRepository;

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

        companyAdmin.setEmail(companyAdminDTOIn.getEmail());
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


        emailService.sendEmail(employee.getEmail(), "New skill Request approved", "Your request with id: "+requestId+ ", has been approved, skill: "
                + skill.getName()+", has been added to the company skills");
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


        emailService.sendEmail(employee.getEmail(), "New skill Request Rejected", "Your request with id: "+requestId+ ", has been rejected, ");
    }

    public List<NewSkillRequestDTOOut> getNewSkillRequestsForAdmin(Integer adminId) {
        CompanyAdmin admin = companyAdminRepository.findCompanyAdminById(adminId);
        if (admin == null) {
            throw new APIException("company admin not found with id: " + adminId);
        }

        List<NewSkillRequest> requests = newSkillRequestRepository.findByCompanyAdminId(adminId);
        List<NewSkillRequestDTOOut> newSkillRequestDTOOuts = new ArrayList<>();

        for (NewSkillRequest r : requests) {
            if(r.getCompanyAdmin() != null){
                newSkillRequestDTOOuts.add(new NewSkillRequestDTOOut(r.getId(), r.getName(), r.getDescription(), r.getStatus(), r.getStart_date(),r.getEnd_date(),r.getCompanyAdmin().getId(),r.getEmployee().getId(),r.getCompany().getId()));
            }else {
                newSkillRequestDTOOuts.add(new NewSkillRequestDTOOut(r.getId(), r.getName(), r.getDescription(), r.getStatus(), r.getStart_date(),r.getEnd_date(),null,r.getEmployee().getId(),r.getCompany().getId()));
            }
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
            newSkillRequestDTOOuts.add(new NewSkillRequestDTOOut(r.getId(), r.getName(), r.getDescription(), r.getStatus(), r.getStart_date(),r.getEnd_date(),null,r.getEmployee().getId(),r.getCompany().getId()));
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

    public void approvedTraining(Integer companyAdminId , Integer requestId){
        CompanyAdmin companyAdmin = companyAdminRepository.findCompanyAdminById(companyAdminId);
        if (companyAdmin == null) {
            throw new APIException("company admin not found with id: " + companyAdminId);
        }

        RequestTraining request = requestTrainingRepository.findRequestTrainingById(requestId);
        if (request == null) {
            throw new APIException("request not found with id: " + requestId);
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
        Skills skills = new Skills();
        skills.setName(request.getName());
        skills.setDescription(request.getName());
        skills.setCompany(employee.getCompany());
        skillsRepository.save(skills);


        Training training = new Training();
        training.setCompany(employee.getCompany());
        training.setDescription(request.getNotes());
        training.setName(request.getName());
        training.setSkills(skills);
        trainingRepository.save(training);


        request.setCompanyAdmin(companyAdmin);
        request.setStatus("approved");
        requestTrainingRepository.save(request);


        emailService.sendEmail(employee.getEmail(), "training Request approved", "Your request with id: "+requestId+ ", has been approved, training: "
                + training.getName()+", has been added to the company training");
    }

    public void rejectRequestTraining(Integer companyAdminId , Integer requestId , RequestTrainingDTOIn requestTrainingDTOIn){
        CompanyAdmin admin = companyAdminRepository.findCompanyAdminById(companyAdminId);
        if (admin == null){
            throw new APIException("company admin not found");
        }
        RequestTraining requestTraining = requestTrainingRepository.findRequestTrainingById(requestId);
        if (requestTraining == null){
            throw new APIException("request training not found");
        }
        Employee employee = employeeRepository.findEmployeeById(requestTraining.getEmployee().getId());

        if (!requestTraining.getEmployee().getCompany().getId().equals(admin.getCompany().getId())){
            throw new APIException("admin company doesn't match the employee company");
        }
        if (!requestTraining.getStatus().equalsIgnoreCase("pending")){
            throw new APIException("request is already checked");
        }
        requestTraining.setStatus("rejected");
        requestTraining.setRejectNote(requestTrainingDTOIn.getRejectNote());
        requestTraining.setEnd_date(LocalDateTime.now());
        requestTrainingRepository.save(requestTraining);


        String answer = aiService.getRejectNoteForEmail(requestTraining.getRejectNote());
        emailService.sendEmail(employee.getEmail(), "Training Request Rejected", "Your request with id: "+requestId+ ", has been rejected, notes: " + answer);
    }




    public CompanyAdmin convertToEntity(CompanyAdminDTOIn dtoIn) {
        return new CompanyAdmin(dtoIn.getCompanyAdmin_id(), dtoIn.getUsername(), dtoIn.getEmail(), dtoIn.getPassword()  ,null, null , null);
    }

    public CompanyAdminDTOOut convertToDTO(CompanyAdmin admin) {
        return new CompanyAdminDTOOut(admin.getId(), admin.getUsername(),admin.getEmail(), admin.getCompany().getId());
    }
}
