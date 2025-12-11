package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.ManagerDTOIn;
import org.example.skillflow.Service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/get-all-managers")
    public ResponseEntity<?> getAllManagers(){
        return ResponseEntity.status(200).body(managerService.getManagers());
    }

    @PostMapping("/create-manager")
    public ResponseEntity<?> createManager(@RequestBody @Valid ManagerDTOIn managerDTOIn){
        managerService.createManager(managerDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the manager was created successfully"));
    }

    @PutMapping("/update-manager/{managerId}")
    public ResponseEntity<?> updateManager(@PathVariable Integer managerId, @RequestBody @Valid ManagerDTOIn managerDTOIn){
        managerService.updateManager(managerId, managerDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the manager was updated successfully"));
    }

    @DeleteMapping("/delete-manager/{company_id}/{manager_id}")
    public ResponseEntity<?> deleteManager(@PathVariable Integer manager_id, @PathVariable Integer company_id){
        managerService.deleteManager(manager_id, company_id);
        return ResponseEntity.status(200).body(new APIResponse("the manager was deleted successfully"));
    }

    @GetMapping("/get-manager-id/{id}")
    public ResponseEntity<?> getManagerById(@PathVariable Integer id){
        return ResponseEntity.status(200).body(managerService.getManagersFullById(id));
    }

    @GetMapping("/get-manager-company/{id}")
    public ResponseEntity<?> getManagerByCompanyId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(managerService.getManagerByCompanyId(id));
    }

    @GetMapping("/get-manager-employee/{id}")
    public ResponseEntity<?> getManagerByEmployeeId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(managerService.getManagerByEmployeeId(id));
    }
}