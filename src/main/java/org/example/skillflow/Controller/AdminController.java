package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.skillflow.API.APIResponse;
<<<<<<< HEAD
=======
import org.example.skillflow.DTO.In.AdminDTOIn;
>>>>>>> test
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
<<<<<<< HEAD
    public ResponseEntity<?> addNewAdmin(@RequestBody @Valid Admin admin){
=======
    public ResponseEntity<?> addNewAdmin(@RequestBody @Valid AdminDTOIn admin){
>>>>>>> test
        adminService.addAdmin(admin);
        return ResponseEntity.status(200).body(new APIResponse("Added Admin successfully"));
    }

    @PutMapping("/update/{adminId}")
<<<<<<< HEAD
    public ResponseEntity<?> updateAdmin(@PathVariable Integer adminId , @RequestBody @Valid Admin admin){
=======
    public ResponseEntity<?> updateAdmin(@PathVariable Integer adminId , @RequestBody @Valid AdminDTOIn admin){
>>>>>>> test
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

<<<<<<< HEAD
=======
    @PutMapping("/applyCompany/{adminId}/{requestId}")
    public ResponseEntity<?> applyCompanyRequest(@PathVariable Integer adminId , @PathVariable Integer requestId){
        adminService.applyRequestCompany(adminId, requestId);
        return ResponseEntity.status(200).body(new APIResponse("Applied company successfully"));
    }

    @GetMapping("/rejectOrder/{adminId}/{requestCompanyId}")
    public ResponseEntity<?> rejectRequestOrder(@PathVariable Integer adminId , @PathVariable Integer requestCompanyId){
        adminService.rejectRequestCompany(adminId, requestCompanyId);
        return ResponseEntity.status(200).body(new APIResponse("rejected request successfully"));
    }
>>>>>>> test
}
