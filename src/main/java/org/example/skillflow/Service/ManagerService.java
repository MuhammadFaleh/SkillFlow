package org.example.skillflow.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {
=======
import org.example.skillflow.DTO.In.EmployeeDTOIn;
import org.example.skillflow.DTO.In.ManagerDTOIn;
import org.example.skillflow.DTO.Out.EmployeeDTOOut;
import org.example.skillflow.DTO.Out.ManagerDTOOut;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Model.Manager;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;

    public List<ManagerDTOOut>



    public ManagerDTOOut convertToDTO(Manager manager){
        return new ManagerDTOOut(manager.getId(),manager.get)
    }

    public Employee convertToEntity(EmployeeDTOIn employeeDTOIn){
        return new Employee(employeeDTOIn.getEmployee_id(),employeeDTOIn.getUsername(),employeeDTOIn.getFull_name(),
                employeeDTOIn.getGender(),employeeDTOIn.getAge(),employeeDTOIn.getEmail(),employeeDTOIn.getPassword(),
                null,null,null,null);
    }

>>>>>>> test
}
