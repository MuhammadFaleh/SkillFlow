package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.EmployeeDTOIn;
import org.example.skillflow.Service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.status(200).body(employeeService.getEmployees());
    }

    @PostMapping("/add")
    public ResponseEntity<?> createEmployee(@RequestBody @Valid EmployeeDTOIn employeeDTOIn){
        employeeService.createEmployee(employeeDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("Added employee successfully"));
    }
}
