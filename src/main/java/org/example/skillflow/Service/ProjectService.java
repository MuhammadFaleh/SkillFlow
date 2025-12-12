package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.ProjectDTOIn;
import org.example.skillflow.DTO.Out.ProjectDTOOut;
import org.example.skillflow.Model.*;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.ProjectRepository;
import org.example.skillflow.Repository.SkillsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;
    private final SkillsRepository skillsRepository;
    private final EmployeeRepository employeeRepository;

    public List<ProjectDTOOut> getProjects() {
        List<ProjectDTOOut> projectDTOOuts = new ArrayList<>();

        for (Project project : projectRepository.findAll()) {
            projectDTOOuts.add(convertToDTO(project));
        }

        return projectDTOOuts;
    }

    public void createProject(ProjectDTOIn projectDTOIn) {
        Company company = companyRepository.findCompanyById(projectDTOIn.getCompany_Id());

        if (company == null){
            throw new APIException("company doesn't exist with id: " + projectDTOIn.getCompany_Id());
        }

        Project project = convertToEntity(projectDTOIn);
        project.setCompany(company);

        project.setStatus("pending");
        project.setStart_date(LocalDateTime.now());
        projectRepository.save(project);
    }

    public void updateProject(Integer projectId, ProjectDTOIn projectDTOIn) {
        Project oldProject = projectRepository.findProjectById(projectId);
        if (oldProject == null){
            throw new APIException("project not found with id: " + projectId);
        }

        Company company = companyRepository.findCompanyById(projectDTOIn.getCompany_Id());
        if (company == null){
            throw new APIException("company doesn't exist with id: " + projectDTOIn.getCompany_Id());
        }

        oldProject.setDescription(projectDTOIn.getDescription());
        oldProject.setStart_date(projectDTOIn.getStart_date());
        oldProject.setEnd_date(projectDTOIn.getEnd_date());
        oldProject.setRisk(projectDTOIn.getRisk());
        oldProject.setCompany(company);

        projectRepository.save(oldProject);
    }

    public void deleteProject(Integer projectId, Integer companyId) {
        Project project = projectRepository.findProjectById(projectId);


        if (project == null) {
            throw new APIException("project not found with id: " + projectId);
        }

        if (!project.getCompany().getId().equals(companyId)) {
            throw new APIException("project does not belong to this company id: " + companyId);
        }

        if (project.getEmployees() != null) {
            for (Employee employee : project.getEmployees()) {
                employee.setProject(null);
                employeeRepository.save(employee);
            }
            project.getEmployees().clear();
        }

        if (project.getProjectManagers() !=null) {
            for (ProjectManager projectManager: project.getProjectManagers()) {

                projectManager.getProjects().remove(project);
            }
            project.getProjectManagers().clear();
        }

        if (project.getSkills()  != null) {
            for (Skills skill : project.getSkills()) {
                skill.getProjects().remove(project);
            }

            project.getSkills().clear();
        }


        projectRepository.delete(project);

    }

    public void assignSkillToProject(Integer projectId, Integer skillId, Integer companyId) {

        Company company = companyRepository.findCompanyById(companyId);
        Project project = projectRepository.findProjectById(projectId);
        Skills skill = skillsRepository.findSkillsById(skillId);

        if (company == null) {
            throw new APIException("company doesn't exist with id: " + companyId);
        }
        if (project == null) {
            throw new APIException("project doesn't exist with id: " + projectId);
        }
        if (skill == null) {
            throw new APIException("skill doesn't exist with id: " + skillId);
        }


        if (project.getCompany() == null || !project.getCompany().getId().equals(companyId)) {
            throw new APIException("project is not in this company: " + companyId);
        }

        if (skill.getCompany() == null || !skill.getCompany().getId().equals(companyId)) {
            throw new APIException("skill is not in this company: " + companyId);
        }


        if (project.getSkills() != null && project.getSkills().contains(skill)) {
            throw new APIException("this skill is already assigned to this project");
        }


        project.getSkills().add(skill);
        skill.getProjects().add(project);


        skillsRepository.save(skill);
        projectRepository.save(project);
    }

    public void unassignSkillFromProject(Integer projectId, Integer skillId, Integer companyId) {
        Company company = companyRepository.findCompanyById(companyId);
        Project project = projectRepository.findProjectById(projectId);
        Skills skill = skillsRepository.findSkillsById(skillId);

        if (company == null) {
            throw new APIException("company doesn't exist with id: " + companyId);
        }
        if (project == null) {
            throw new APIException("project doesn't exist with id: " + projectId);
        }
        if (skill == null) {
            throw new APIException("skill doesn't exist with id: " + skillId);
        }

        if (project.getCompany() == null || !project.getCompany().getId().equals(companyId) || skill.getCompany() == null || !skill.getCompany().getId().equals(companyId)) {
            throw new APIException("project or skill are not in this company");
        }

        if (project.getSkills() == null || !project.getSkills().contains(skill)) {
            throw new APIException("skill is not assigned to this project");
        }

        project.getSkills().remove(skill);
        skill.getProjects().remove(project);

        skillsRepository.save(skill);
        projectRepository.save(project);
    }


    public void assignEmployeeToProject(Integer projectId, Integer employeeId, Integer companyId) {

        Company company = companyRepository.findCompanyById(companyId);
        Project project = projectRepository.findProjectById(projectId);
        Employee employee = employeeRepository.findEmployeeById(employeeId);

        if (company == null) {
            throw new APIException("company doesn't exist with id: " + companyId);
        }
        if (project == null) {
            throw new APIException("project doesn't exist with id: " + projectId);
        }
        if (employee == null) {
            throw new APIException("employee doesn't exist with id: " + employeeId);
        }

        if (project.getCompany() == null || !project.getCompany().getId().equals(companyId)) {
            throw new APIException("project is not in this company: " + companyId);
        }
        if (employee.getCompany() == null || !employee.getCompany().getId().equals(companyId)) {
            throw new APIException("employee is not in this company: " + companyId);
        }

        if (employee.getProject() != null) {
            throw new APIException("employee is already assigned to a project, unassign first");
        }

        employee.setProject(project);
        project.getEmployees().add(employee);

        employeeRepository.save(employee);
        projectRepository.save(project);

        //for testing later
        //emailService.sendEmail(employee.getEmail(), "Request approved", "Your request with id: "+requestId+ ", has been approved");
    }


    public void unassignEmployeeFromProject(Integer projectId, Integer employeeId, Integer companyId) {
        Company company = companyRepository.findCompanyById(companyId);
        Project project = projectRepository.findProjectById(projectId);
        Employee employee = employeeRepository.findEmployeeById(employeeId);

        if (company == null) {
            throw new APIException("company doesn't exist with id: " + companyId);
        }
        if (project == null) {
            throw new APIException("project doesn't exist with id: " + projectId);
        }
        if (employee == null) {
            throw new APIException("employee doesn't exist with id: " + employeeId);
        }

        if (project.getCompany() == null || !project.getCompany().getId().equals(companyId) || employee.getCompany() == null || !employee.getCompany().getId().equals(companyId)) {
            throw new APIException("project or employee are not in this company");
        }


        if (employee.getProject() == null || !employee.getProject().getId().equals(projectId)) {
            throw new APIException("employee is not assigned to this project");
        }

        employee.setProject(null);

        if (project.getEmployees() != null) {
            project.getEmployees().remove(employee);
        }

        employeeRepository.save(employee);
        projectRepository.save(project);
    }

    public void completeProject(Integer projectId, Integer companyId) {


        Project project = projectRepository.findProjectById(projectId);
        if (project == null) {
            throw new APIException("project not found with id: " + projectId);
        }


        if (!project.getCompany().getId().equals(companyId)) {
            throw new APIException("project does not belong to this company id: " + companyId);

        }


        if (!project.getStatus().equalsIgnoreCase("in_progress")) {
            throw new APIException("only in_progress projects can be completed");
        }

        int projectRisk = getRiskPercentageForProject(project);


        if (project.getProjectManagers() != null) {
            for (ProjectManager projectManager : project.getProjectManagers()) {

                int currentLoad;
                if (projectManager.getRisk_load() == null) {
                    currentLoad = 0;
                } else {
                    currentLoad = projectManager.getRisk_load();
                }

                int newRiskLoad = currentLoad - projectRisk;

                projectManager.setRisk_load(newRiskLoad);
            }
        }

        project.setStatus("completed");
        project.setEnd_date(LocalDateTime.now());

        projectRepository.save(project);
    }

    public ProjectDTOOut getProjectById(Integer projectId) {
        Project project = projectRepository.findProjectById(projectId);

        if (project == null) {
            throw new APIException("project not found with id: " + projectId);
        }

        return convertToDTO(project);
    }

    public List<ProjectDTOOut> getProjectsByCompany(Integer companyId) {
        List<Project> projects = projectRepository.findByCompanyId(companyId);
        List<ProjectDTOOut> projectDTOOuts = new ArrayList<>();

        for (Project p : projects) {
            projectDTOOuts.add(convertToDTO(p));
        }

        return projectDTOOuts;
    }

    public List<ProjectDTOOut> getProjectsByCompanyAndStatus(Integer companyId, String status) {
        List<Project> projects = projectRepository.findByCompanyIdAndStatus(companyId, status);
        List<ProjectDTOOut> projectDTOOuts = new ArrayList<>();

        for (Project p : projects) {
            projectDTOOuts.add(convertToDTO(p));
        }

        return projectDTOOuts;
    }

    public List<ProjectDTOOut> getProjectsByCompanyAndRisk(Integer companyId, String riskLevel) {
        List<Project> projects = projectRepository.findByCompanyIdAndRisk(companyId, riskLevel);
        List<ProjectDTOOut> projectDTOOuts = new ArrayList<>();

        for (Project p : projects) {
            projectDTOOuts.add(convertToDTO(p));
        }

        return projectDTOOuts;
    }

    private int getRiskPercentageForProject(Project project) {

        if (project.getRisk() == null) {
            return 0;
        }

        return switch (project.getRisk().toLowerCase()) {
            case "low" -> 25;
            case "medium" -> 50;
            case "high" -> 75;
            case "critical" -> 100;
            default -> throw new APIException("invalid project risk value: " + project.getRisk());
        };

    }
    public Project convertToEntity(ProjectDTOIn projectDTOIn) {
        return new Project(projectDTOIn.getProject_id(), projectDTOIn.getDescription(), null, projectDTOIn.getStart_date(), projectDTOIn.getEnd_date(), projectDTOIn.getRisk(),
                null, null, null, null );
    }

    public ProjectDTOOut convertToDTO(Project project) {
        return new ProjectDTOOut(project.getId(), project.getDescription(), project.getStatus(), project.getStart_date(), project.getEnd_date(), project.getRisk());
    }
}