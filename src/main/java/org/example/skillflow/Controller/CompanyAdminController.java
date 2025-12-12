package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.CompanyAdminDTOIn;
import org.example.skillflow.Service.CompanyAdminService;
import org.example.skillflow.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companyAdmin")
@RequiredArgsConstructor
public class CompanyAdminController {

    private final CompanyAdminService companyAdminService;


    @GetMapping("/get")
    public ResponseEntity<?> getCompanyAdmins() {
        return ResponseEntity.status(200).body(companyAdminService.getCompanyAdmins());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCompanyAdmin(@RequestBody @Valid CompanyAdminDTOIn companyAdminDTOIn) {
        companyAdminService.createCompanyAdmin(companyAdminDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("company admin has been created with username: "+ companyAdminDTOIn.getUsername()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompanyAdmin(@PathVariable Integer id,@RequestBody @Valid CompanyAdminDTOIn companyAdminDTOIn) {
        companyAdminService.updateCompanyAdmin(id , companyAdminDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("company admin has been updated with id: "+ id) );
    }

    @DeleteMapping("/delete/{id}/{companyId}")
    public ResponseEntity<?> deleteCompanyAdmin(@PathVariable Integer id, @PathVariable Integer companyId) {
        companyAdminService.deleteCompanyAdmin(id ,companyId);

        return ResponseEntity.status(200).body(new APIResponse("company admin has been deleted with id: "+ id));
    }

    @PutMapping("/approve-project/{adminId}/{projectId}/{companyId}")
    public ResponseEntity<?> approveProject(@PathVariable Integer adminId,@PathVariable Integer projectId,@PathVariable Integer companyId) {
        companyAdminService.approveProject(adminId, projectId, companyId);

        return ResponseEntity.status(200).body(new APIResponse("project approved: " + projectId));
    }

    @PutMapping("/start-project/{adminId}/{projectId}/{companyId}")
    public ResponseEntity<?> startProject(@PathVariable Integer adminId,@PathVariable Integer projectId,@PathVariable Integer companyId) {
        companyAdminService.startProject(adminId, projectId, companyId);

        return ResponseEntity.status(200).body(new APIResponse("project started (in_progress): " + projectId));
    }

    @PutMapping("/reject-project/{adminId}/{projectId}/{companyId}")
    public ResponseEntity<?> rejectProject(@PathVariable Integer adminId,@PathVariable Integer projectId,@PathVariable Integer companyId) {
        companyAdminService.rejectProject(adminId, projectId, companyId);

        return ResponseEntity.status(200).body(new APIResponse("project rejected: " + projectId));
    }

    @PostMapping("/approve-new-skill/{CompanyAdminId}/{requestId}")
    public ResponseEntity<?> approveNewSkill(@PathVariable Integer CompanyAdminId, @PathVariable Integer requestId) {
        companyAdminService.approveNewSkillRequest(CompanyAdminId , requestId);
        return ResponseEntity.status(200).body(new APIResponse("skill request with id: "+ requestId+", has been approved and added to skills"));
    }

    @PutMapping("/reject-new-skill/{CompanyAdminId}/{requestId}")
    public ResponseEntity<?> rejectNewSkill(@PathVariable Integer CompanyAdminId, @PathVariable Integer requestId) {
        companyAdminService.rejectNewSkillRequest(CompanyAdminId, requestId);

        return ResponseEntity.status(200).body(new APIResponse("skill request with id: "+ requestId+", has been rejected"));
    }

    @GetMapping("/new-skill-requests/{CompanyAdminId}")
    public ResponseEntity<?> getAdminNewSkillRequests(@PathVariable Integer CompanyAdminId) {
        return ResponseEntity.status(200).body(companyAdminService.getNewSkillRequestsForAdmin(CompanyAdminId));
    }

    @GetMapping("/pending-requests/{CompanyAdminId}")
    public ResponseEntity<?> getAdminPendingRequests(@PathVariable Integer CompanyAdminId) {
        return ResponseEntity.status(200).body(companyAdminService.getPendingNewSkillRequestsForAdmin(CompanyAdminId));
    }

}