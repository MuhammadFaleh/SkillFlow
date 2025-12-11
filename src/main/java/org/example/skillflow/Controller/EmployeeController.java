package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.EmployeeDTOIn;
import org.example.skillflow.Service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/get-all-employees")
    public ResponseEntity<?> getAllEmployees(){
        return ResponseEntity.status(200).body(employeeService.getEmployees());
    }

    @PostMapping("/create-employee")
    public ResponseEntity<?> createEmployee(@RequestBody @Valid EmployeeDTOIn employeeDTOIn){
        employeeService.createEmployee(employeeDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the employee was created successfully"));
    }

    @PutMapping("/update-employee/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer employeeId, @RequestBody @Valid EmployeeDTOIn employeeDTOIn){
        employeeService.updateEmployee(employeeId, employeeDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the employee was updated successfully"));
    }

    @DeleteMapping("/delete-employee/{employeeId}/{companyId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer employeeId, @PathVariable Integer companyId){
        employeeService.deleteEmployee(employeeId, companyId);
        return ResponseEntity.status(200).body(new APIResponse("the employee was deleted successfully"));
    }

    @PutMapping("/assign-manager/{companyId}/{employeeId}/{managerId}")
    public ResponseEntity<?> assignManager(@PathVariable Integer companyId, @PathVariable Integer employeeId, @PathVariable Integer managerId){
        employeeService.assignManager(companyId, employeeId, managerId);
        return ResponseEntity.status(200).body(new APIResponse("the manager was assigned successfully"));
    }

    @PutMapping("/unassign-manager/{companyId}/{employeeId}/{managerId}")
    public ResponseEntity<?> unassignManager(@PathVariable Integer companyId, @PathVariable Integer employeeId, @PathVariable Integer managerId){
        employeeService.unassignManager(companyId, employeeId, managerId);
        return ResponseEntity.status(200).body(new APIResponse("the manager was unassigned successfully"));
    }

    @GetMapping("/get-employees-by-manager/{managerId}")
    public ResponseEntity<?> getEmployeesByManagerId(@PathVariable Integer managerId){
        return ResponseEntity.status(200).body(employeeService.getEmployeesByManagerId(managerId));
    }

    @GetMapping("/get-employees-by-company/{companyId}")
    public ResponseEntity<?> getEmployeesByCompanyId(@PathVariable Integer companyId){
        return ResponseEntity.status(200).body(employeeService.getEmployeesByCompanyId(companyId));
    }
}