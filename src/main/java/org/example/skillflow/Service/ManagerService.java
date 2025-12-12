package org.example.skillflow.Service;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.EmployeeDTOIn;
import org.example.skillflow.DTO.In.ManagerDTOIn;
import org.example.skillflow.DTO.Out.*;
import org.example.skillflow.Model.*;
import org.example.skillflow.Repository.AddSkillRequestRepository;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final EmployeeRepository employeeRepository;
    private final AddSkillRequestRepository addSkillRequestRepository;
    private final ManagerRepository managerRepository;
    private final CompanyRepository companyRepository;

    public List<ManagerDTOOut> getManagers(){
        List<ManagerDTOOut> managerDTOOuts = new ArrayList<>();
        for (Manager manager : managerRepository.findAll()){
            managerDTOOuts.add(convertToDTO(manager));
        }
        return managerDTOOuts;
    }

    public void createManager(ManagerDTOIn managerDTOIn){
        Company company = companyRepository.findCompanyById(managerDTOIn.getCompany_id());

        if(company == null){
            throw new APIException("company not found");
        }
        Manager manager = convertToEntity(managerDTOIn);
        manager.setCompany(company);
        managerRepository.save(manager);
    }

    public void updateManager(Integer id, ManagerDTOIn managerDTOIn){
        Company company = companyRepository.findCompanyById(managerDTOIn.getCompany_id());
        Manager manager = managerRepository.findManagerById(id);

        if(company == null || manager == null || company.getId().equals(manager.getCompany().getId())){
            throw new APIException("company or manager not found");
        }
        manager.setAge(managerDTOIn.getAge());
        manager.setEmail(managerDTOIn.getEmail());
        manager.setFull_name(managerDTOIn.getFull_name());
        manager.setPassword(managerDTOIn.getPassword());

        managerRepository.save(manager);
    }

    public void deleteManager(Integer id, Integer company_id){
        Company company = companyRepository.findCompanyById(company_id);
        Manager manager = managerRepository.findManagerById(id);

        if(company == null || manager == null || !company.getId().equals(manager.getCompany().getId())){
            throw new APIException("company or manager not found");
        }
        for(Employee employee : manager.getEmployee()){
            employee.setManager(null);
            employeeRepository.save(employee);
        }
        manager.getEmployee().clear();
        company.getManager().remove(manager);
        addSkillRequestRepository.deleteByManagerId(manager.getId());
        companyRepository.save(company);
        managerRepository.delete(manager);
    }

    public ManagerFullDTOOut getManagersFullById(Integer id){
        Manager manager = managerRepository.findManagerById(id);
        if(manager == null){
            return null;
        }
        if(manager.getEmployee() == null){
            return new ManagerFullDTOOut(manager.getId(), manager.getUsername(),manager.getFull_name(),manager.getGender(),
                    manager.getAge(),manager.getEmail(),manager.getCompany().getId(),null);
        }
        return new ManagerFullDTOOut(manager.getId(), manager.getUsername(),manager.getFull_name(),manager.getGender(),
                manager.getAge(),manager.getEmail(),manager.getCompany().getId(),convertEmployeeShortToDTOOut(manager.getEmployee()));
    }

    public List<ManagerDTOOut> getManagerByCompanyId(Integer id){
        List<ManagerDTOOut> managerDTOOuts = new ArrayList<>();
        for (Manager manager : managerRepository.findMangersByCompanyId(id)){
            managerDTOOuts.add(convertToDTO(manager));
        }
        return managerDTOOuts;
    }

    public ManagerDTOOut getManagerByEmployeeId(Integer id){
        return convertToDTO(managerRepository.findManagerByEmployeeId(id));
    }

    public Set<EmployeeShortDTOOut> convertEmployeeShortToDTOOut(Set<Employee> employee){
        Set<EmployeeShortDTOOut> employeeShortDTOOut = new HashSet<>();
        for (Employee e : employee){
            employeeShortDTOOut.add(new EmployeeShortDTOOut(e.getId(),e.getUsername(),e.getEmail()));
        }
        return employeeShortDTOOut;
    }

    public ManagerDTOOut convertToDTO(Manager manager){
        return new ManagerDTOOut(manager.getId(),manager.getUsername(),
                manager.getFull_name(),manager.getGender(),manager.getAge(),
                manager.getEmail(),manager.getCompany().getId());
    }

    public Manager convertToEntity(ManagerDTOIn managerDTOIn){
        return new Manager(null,managerDTOIn.getUsername(),managerDTOIn.getFull_name(),
                managerDTOIn.getGender(),managerDTOIn.getAge(),managerDTOIn.getEmail(),managerDTOIn.getPassword(),
                null, null ,null,null);
    }

}
