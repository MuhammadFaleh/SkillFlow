package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.Model.Admin;
import org.example.skillflow.Service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllAdmins(){
        return ResponseEntity.status(200).body(adminService.getAllAdmin());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewAdmin(@RequestBody @Valid Admin admin){
        adminService.addAdmin(admin);
        return ResponseEntity.status(200).body(new APIResponse("Added Admin successfully"));
    }

    @PutMapping("/update/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer adminId , @RequestBody @Valid Admin admin){
        adminService.updateAdmin(adminId, admin);
        return ResponseEntity.status(200).body(new APIResponse("updated admin successfully"));
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer adminId){
        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(200).body(new APIResponse("Delete admin successfully"));
    }

    @GetMapping("/getRequestStatusPending")
    public ResponseEntity<?> getRequestCompany(){
        return ResponseEntity.status(200).body(adminService.getCompanyRequestByStatus());
    }

}
