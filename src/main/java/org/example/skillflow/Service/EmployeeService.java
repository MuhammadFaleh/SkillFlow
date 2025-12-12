package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.EmployeeDTOIn;
import org.example.skillflow.DTO.Out.EmployeeDTOOut;
import org.example.skillflow.DTO.Out.EmployeeSkillsDTOOut;
import org.example.skillflow.DTO.Out.SkillsDTOOut;
import org.example.skillflow.Model.*;
import org.example.skillflow.Repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final ManagerRepository managerRepository;
    private final AddSkillRequestRepository addSkillRequestRepository;
    private final SkillsRepository skillsRepository;
    private final ProjectRepository projectRepository;
    private final TrainingSessionRepository sessionRepository;
    private final TrainingEnrollRequestRepository requestRepository;

    public List<EmployeeDTOOut> getEmployees(){
        List<EmployeeDTOOut> employeeDTOOuts = new ArrayList<>();
        for (Employee employee : employeeRepository.findAll()){
            employeeDTOOuts.add(convertToDTO(employee));
        }
        return employeeDTOOuts;
    }

    public void createEmployee(EmployeeDTOIn employeeDTOIn){
        Company company = companyRepository.findCompanyById(employeeDTOIn.getCompany_id());
        if(company == null){
            throw new APIException("company doesn't exist");
        }
        Employee employee = convertToEntity(employeeDTOIn);
        employee.setCompany(company);
        // set skills
        // set project
        employeeRepository.save(employee);
    }

    public void updateEmployee(Integer id, EmployeeDTOIn employeeDTOIn){

        Company company = companyRepository.findCompanyById(employeeDTOIn.getCompany_id());
        Employee employee = employeeRepository.findEmployeeById(id);

        if(employee == null){
            throw new APIException("employee or manger doesn't exist");
        }

        if(company == null){
            throw new APIException("company doesn't exist");
        }
        if(!employeeDTOIn.getEmail().equalsIgnoreCase(employee.getEmail())){
            employee.setEmail(employeeDTOIn.getEmail());
        }
        if(!employeeDTOIn.getUsername().equalsIgnoreCase(employee.getUsername())){
            employee.setUsername(employeeDTOIn.getUsername());
        }
        employee.setAge(employeeDTOIn.getAge());
        employee.setFull_name(employeeDTOIn.getFull_name());
        employee.setPassword(employeeDTOIn.getPassword());
        employeeRepository.save(employee);
    }

    public void deleteEmployee(Integer id, Integer company_id){

        Company company = companyRepository.findCompanyById(company_id);
        Employee employee = employeeRepository.findEmployeeById(id);


        if(employee == null || !employee.getCompany().getId().equals(company_id)){
            throw new APIException("employee or manger doesn't exist");
        }

        if(company == null){
            throw new APIException("company doesn't exist");
        }
        Manager manager = managerRepository.findManagerById(employee.getManager().getId());
        if(manager != null){
            manager.getEmployee().remove(employee);
            managerRepository.save(manager);
        }
        // unsign from project and skill training
        addSkillRequestRepository.deleteByEmployeeId(employee.getId());
        requestRepository.deleteByEmployeeId(employee.getId());
        sessionRepository.deleteByEmployeeId(employee.getId());
        removeSkills(employee);
        employee.setProject(null);
        Project project = projectRepository.findProjectByCompanyIdAndEmployeesId(company_id,id);
        project.getEmployees().remove(employee);
        projectRepository.save(project);
        employeeRepository.delete(employee);
    }

    public void removeSkills(Employee employee){
        for (Skills skills : employee.getSkills()){
            skills.getEmployee().remove(employee);
            skillsRepository.save(skills);
        }
    }

    public void assignManager(Integer company_id, Integer employee_id, Integer manger_id){

        Company company = companyRepository.findCompanyById(company_id);
        Manager manager = managerRepository.findManagerById(manger_id);
        Employee employee = employeeRepository.findEmployeeById(employee_id);

        if(company == null){
            throw new APIException("company doesn't exist");
        }

        if(employee == null || manager == null){
            throw new APIException("employee or manger doesn't exist");
        }

        if(!employee.getCompany().getId().equals(manager.getCompany().getId())
                || !company_id.equals(employee.getCompany().getId())){
            throw new APIException("employee or manger not in the same company");
        }

        if(employee.getManager() != null){
            throw new APIException("employee has a manager please remove it first");
        }

        manager.getEmployee().add(employee);
        employee.setManager(manager);
        managerRepository.save(manager);
        employeeRepository.save(employee);
    }

    public void unassignManager(Integer company_id, Integer employee_id, Integer manger_id){

        Company company = companyRepository.findCompanyById(company_id);
        Manager manager = managerRepository.findManagerById(manger_id);
        Employee employee = employeeRepository.findEmployeeById(employee_id);

        if(company == null){
            throw new APIException("company doesn't exist");
        }

        if(employee == null || manager == null){
            throw new APIException("employee or manager doesn't exist");
        }

        if(!employee.getCompany().getId().equals(manager.getCompany().getId())
                || !company_id.equals(employee.getCompany().getId())){
            throw new APIException("employee or manger not in the same company");
        }

        if(!employee.getManager().getId().equals(manager.getId())){
            throw new APIException("not the same manager and employee");
        }

        employee.setManager(null);
        manager.getEmployee().remove(employee);
        managerRepository.save(manager);
        employeeRepository.save(employee);
    }

    public List<EmployeeSkillsDTOOut> getEmployeesByManagerId(Integer id){
        List<EmployeeSkillsDTOOut> employeeDTOOuts = new ArrayList<>();
        for (Employee employee : employeeRepository.findEmployeeByManagerId(id)){
            employeeDTOOuts.add(convertShortToDTO(employee));
        }
        return employeeDTOOuts;
    }

    public List<EmployeeSkillsDTOOut> getEmployeesByCompanyId(Integer id){
        List<EmployeeSkillsDTOOut> employeeDTOOuts = new ArrayList<>();
        for (Employee employee : employeeRepository.findEmployeeByCompanyId(id)){
            employeeDTOOuts.add(convertShortToDTO(employee));
        }
        return employeeDTOOuts;
    }

    public EmployeeSkillsDTOOut convertShortToDTO(Employee employee){
        if(employee.getManager() == null){
            return new EmployeeSkillsDTOOut(employee.getId(),employee.getUsername(),employee.getFull_name(),
                    employee.getEmail(),null,convertSkillsToDTOOut(employee.getSkills()));
        }
        return new EmployeeSkillsDTOOut(employee.getId(),employee.getUsername(),employee.getFull_name(),
                employee.getEmail(),employee.getManager().getId(),convertSkillsToDTOOut(employee.getSkills()));

    }

    public Set<SkillsDTOOut> convertSkillsToDTOOut(Set<Skills> skills){
        Set<SkillsDTOOut> skillsDTOOuts = new HashSet<>();
        for (Skills s : skills){
            skillsDTOOuts.add(new SkillsDTOOut(s.getId(),s.getName(),
                    s.getDescription(),s.getCompany().getId()));
        }
        return skillsDTOOuts;
    }

    public EmployeeDTOOut convertToDTO(Employee employee){
        return new EmployeeDTOOut(employee.getId(),employee.getUsername(),employee.getFull_name(),
                employee.getGender(),employee.getAge(),employee.getEmail(), employee.getCompany().getId());
    }

    public Employee convertToEntity(EmployeeDTOIn employeeDTOIn){
        return new Employee(null ,employeeDTOIn.getUsername(),employeeDTOIn.getFull_name(),
                employeeDTOIn.getGender(),employeeDTOIn.getAge(),employeeDTOIn.getEmail(),employeeDTOIn.getPassword(),
                null,null,null,null,null,null,null,null);
    }
}
