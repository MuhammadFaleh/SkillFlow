package org.example.skillflow.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.DTO.In.EmployeeDTOIn;
import org.example.skillflow.DTO.Out.EmployeeDTOOut;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<EmployeeDTOOut> getEmployees(){
        List<EmployeeDTOOut> employeeDTOOuts = new ArrayList<>();
        for (Employee employee : employeeRepository.findAll()){
            employeeDTOOuts.add(convertToDTO(employee));
        }
        return employeeDTOOuts;
    }

    public void createEmployee(EmployeeDTOIn employeeDTOIn){
        
    }

    public EmployeeDTOOut convertToDTO(Employee employee){
        return new EmployeeDTOOut(employee.getId(),employee.getUsername(),employee.getFull_name(),
                employee.getGender(),employee.getAge(),employee.getEmail());
    }
}
