package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.EmployeeDTOIn;
import org.example.skillflow.DTO.Out.EmployeeDTOOut;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Model.Manager;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final ManagerRepository managerRepository;

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

        employee.setAge(employeeDTOIn.getAge());
        employee.setEmail(employeeDTOIn.getEmail());
        employee.setFull_name(employeeDTOIn.getFull_name());
        employee.setPassword(employeeDTOIn.getPassword());

        employeeRepository.save(convertToEntity(employeeDTOIn));
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
        // unsign from project and skill
        employeeRepository.delete(employee);
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
        managerRepository.save(manager);
    }

    public EmployeeDTOOut convertToDTO(Employee employee){
        return new EmployeeDTOOut(employee.getId(),employee.getUsername(),employee.getFull_name(),
                employee.getGender(),employee.getAge(),employee.getEmail(), employee.getCompany().getId());
    }

    public Employee convertToEntity(EmployeeDTOIn employeeDTOIn){
        return new Employee(employeeDTOIn.getEmployee_id(),employeeDTOIn.getUsername(),employeeDTOIn.getFull_name(),
                employeeDTOIn.getGender(),employeeDTOIn.getAge(),employeeDTOIn.getEmail(),employeeDTOIn.getPassword(),
                null,null,null,null,null);
    }
}
