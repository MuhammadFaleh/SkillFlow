package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.ProjectManagerDTOIn;
import org.example.skillflow.DTO.Out.ProjectManagerDTOOut;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.Project;
import org.example.skillflow.Model.ProjectManager;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.ProjectManagerRepository;
import org.example.skillflow.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectManagerService {
    private final ProjectManagerRepository projectManagerRepository;
    private final CompanyRepository companyRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    public List<ProjectManagerDTOOut> getProjectManagers(){
        List<ProjectManagerDTOOut> projectManagerDTOOuts = new ArrayList<>();

        for(ProjectManager projectManager: projectManagerRepository.findAll()){
            projectManagerDTOOuts.add(convertToDTO(projectManager));
        }
        return projectManagerDTOOuts;
    }

    public void createProjectManager(ProjectManagerDTOIn projectManagerDTOIn){
        Company company = companyRepository.findCompanyById(projectManagerDTOIn.getCompany_id());
        if(company == null){
            throw new APIException("company doesn't exist");
        }

        ProjectManager projectManager = convertToEntity(projectManagerDTOIn);
        projectManager.setCompany(company);
        projectManagerRepository.save(projectManager);
    }

    public void updateProjectManager(Integer id,ProjectManagerDTOIn projectManagerDTOIn){
        Company company = companyRepository.findCompanyById(projectManagerDTOIn.getCompany_id());
        ProjectManager projectManager = projectManagerRepository.findProjectManagerById(id);
        if(company == null){
            throw new APIException("company doesn't exist");
        }

        if(projectManager == null){
            throw new APIException("Project manager doesn't exist");
        }
        projectManager.setEmail(projectManagerDTOIn.getEmail());
        projectManager.setUsername(projectManagerDTOIn.getUsername());
        projectManager.setPassword(projectManagerDTOIn.getPassword());
        projectManager.setFull_name(projectManagerDTOIn.getFull_name());
        projectManager.setAge(projectManagerDTOIn.getAge());
        projectManager.setGender(projectManagerDTOIn.getGender());
        projectManager.setRisk_load(projectManagerDTOIn.getRisk_load());

        projectManagerRepository.save(projectManager);
    }

    public void deleteProjectManager(Integer id,Integer company_id){
        Company company = companyRepository.findCompanyById(company_id);
        ProjectManager projectManager = projectManagerRepository.findProjectManagerById(id);
        if(company == null){
            throw new APIException("company doesn't exist");
        }

        if(projectManager == null || !projectManager.getCompany().getId().equals(company_id)){
            throw new APIException("Project manager doesn't exist");
        }

        //must unassign first
        if (projectManager.getProjects() != null && !projectManager.getProjects().isEmpty()) {
            throw new APIException("cannot delete project manager while assigned to projects");
        }

        projectManagerRepository.delete(projectManager);
    }

    public void assignProjectToManager(Integer projectManagerId, Integer projectId, Integer companyId) {

        Company company = companyRepository.findCompanyById(companyId);
        ProjectManager projectManager = projectManagerRepository.findProjectManagerById(projectManagerId);
        Project project = projectRepository.findProjectById(projectId);


        if (company == null) {
            throw new APIException("company doesn't exist with id: " + companyId );
        }

        if (projectManager == null) {
            throw new APIException("project manager doesn't exist with id: " + projectManagerId);
        }

        if (project == null) {
            throw new APIException("project doesn't exist with id: "+ projectId);
        }


        //checks if project manager belongs to given company id
        if (projectManager.getCompany() == null || !projectManager.getCompany().getId().equals(companyId)) {
            throw new APIException("project manager is not in this company: " + companyId );
        }
        //checks if project belongs to given company id
        if (project.getCompany() == null || !project.getCompany().getId().equals(companyId)) {
            throw new APIException("project is not in this company: " + companyId );
        }

        // this makes sure that we dont assign the same project to a project manager twice, btw i made it waaay too simple using contains, turns out you can use it in sets.
        if (project.getProjectManagers().contains(projectManager)) {
            throw new APIException("this project is already assigned to this project manager with id: " + projectManagerId);
        }

        int projectRisk = getRiskPercentageForProject(project);
        int currentLoad = projectManager.getRisk_load() == null ? 0 : projectManager.getRisk_load();
        int newRiskLoad = currentLoad + projectRisk;

        if (newRiskLoad > 100) {
            throw new APIException("cannot assign project: risk load would exceed 100 , current risk = " + currentLoad + ", project = " + projectRisk );
        }

        projectManager.setRisk_load(newRiskLoad);

        project.getProjectManagers().add(projectManager);
        projectManager.getProjects().add(project);

        projectManagerRepository.save(projectManager);
        projectRepository.save(project);

        //for testing later
//        emailService.sendEmail(projectManager.getEmail(), "You have been assign to a project", "You have been assign to a project with id: "+ projectId+
//                ", project details: " +project.getDescription() + ", project risk, "+ project.getRisk() + ", project status: "+ project.getStatus());
    }

    public void unassignProjectFromManager(Integer projectManagerId, Integer projectId, Integer companyId) {
        Company company = companyRepository.findCompanyById(companyId);
        ProjectManager projectManager = projectManagerRepository.findProjectManagerById(projectManagerId);
        Project project = projectRepository.findProjectById(projectId);

        if (company == null){
            throw new APIException("company doesn't exist with id: " + companyId );
        }
        if (projectManager == null) {
            throw new APIException("project manager doesn't exist with id:" + projectManagerId );
        }
        if (project == null) {
            throw new APIException("project doesn't exist with id: "+ projectId );
        }

        if (!projectManager.getCompany().getId().equals(companyId) || !project.getCompany().getId().equals(companyId)) {
            throw new APIException("project or project manager are not in this company");
        }

        // opposite of the last if in assignProjectToManager
        if (!project.getProjectManagers().contains(projectManager)) {
            throw new APIException("Manager with id: "+ projectManagerId+", is not assigned to this project");
        }

        int projectRisk = getRiskPercentageForProject(project);
        int currentLoad = projectManager.getRisk_load() == null ? 0 : projectManager.getRisk_load();
        int newRiskLoad = currentLoad - projectRisk;


        projectManager.setRisk_load(newRiskLoad);

        project.getProjectManagers().remove(projectManager);
        projectManager.getProjects().remove(project);
        projectManagerRepository.save(projectManager);
        projectRepository.save(project);
    }

    public ProjectManagerDTOOut getProjectManagerById(Integer id) {
        ProjectManager projectManager = projectManagerRepository.findProjectManagerById(id);
        if (projectManager == null) {
            throw new APIException("project manager not found with id: " + id);
        }
        return convertToDTO(projectManager);
    }

    public List<ProjectManagerDTOOut> getProjectManagersByCompany(Integer companyId) {
        List<ProjectManager> managers = projectManagerRepository.findByCompanyId(companyId);
        List<ProjectManagerDTOOut> projectManagerDTOOuts = new ArrayList<>();

        for (ProjectManager p : managers) {
            projectManagerDTOOuts.add(convertToDTO(p));
        }

        return projectManagerDTOOuts;
    }

    public List<ProjectManagerDTOOut> getProjectManagersByCompanyAndRiskOver(Integer companyId, Integer limit) {
        List<ProjectManager> managers = projectManagerRepository.findByCompanyIdAndRiskLoadGreaterThanEqual(companyId, limit);
        List<ProjectManagerDTOOut> projectManagerDTOOuts = new ArrayList<>();

        for (ProjectManager pm : managers) {
            projectManagerDTOOuts.add(convertToDTO(pm));
        }

        return projectManagerDTOOuts;
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

    public ProjectManager convertToEntity(ProjectManagerDTOIn projectManagerDTOIn){
        return new ProjectManager(projectManagerDTOIn.getProject_Manager_id(),projectManagerDTOIn.getUsername(),projectManagerDTOIn.getPassword(),projectManagerDTOIn.getFull_name(),projectManagerDTOIn.getGender(),projectManagerDTOIn.getAge(), projectManagerDTOIn.getEmail(),projectManagerDTOIn.getRisk_load(),null, null);
    }

    public ProjectManagerDTOOut convertToDTO(ProjectManager projectManager){
        return new ProjectManagerDTOOut(projectManager.getId(),projectManager.getUsername(),projectManager.getPassword(),projectManager.getFull_name(),projectManager.getGender(),projectManager.getAge(),projectManager.getEmail(),projectManager.getRisk_load(),projectManager.getCompany().getId());
    }
}
