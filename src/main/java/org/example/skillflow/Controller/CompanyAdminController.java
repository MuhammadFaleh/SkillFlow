package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.CompanyAdminDTOIn;
import org.example.skillflow.DTO.In.RequestTrainingDTOIn;
import org.example.skillflow.Service.CompanyAdminService;
import org.example.skillflow.vaildationGroups.ValidationGroup1;
import org.example.skillflow.vaildationGroups.ValidationGroup2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company-admin")
@RequiredArgsConstructor
public class CompanyAdminController {

    private final CompanyAdminService companyAdminService;

    @GetMapping("/get-company-admin")
    public ResponseEntity<?> getCompanyAdmins() {
        return ResponseEntity.status(200).body(companyAdminService.getCompanyAdmins());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCompanyAdmin(@RequestBody @Validated(ValidationGroup1.class) CompanyAdminDTOIn companyAdminDTOIn) {
        companyAdminService.createCompanyAdmin(companyAdminDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("company admin has been created with id: "+ companyAdminDTOIn.getCompanyAdmin_id()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompanyAdmin(@PathVariable Integer id,@RequestBody  @Validated(ValidationGroup1.class) CompanyAdminDTOIn companyAdminDTOIn) {
        companyAdminService.updateCompanyAdmin(id , companyAdminDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("company admin has been updated with id: "+ id) );
    }

    @DeleteMapping("/delete/{id}/{companyId}")
    public ResponseEntity<?> deleteCompanyAdmin(@PathVariable Integer id, @PathVariable Integer companyId) {
        companyAdminService.deleteCompanyAdmin(id ,companyId);
        return ResponseEntity.status(200).body(new APIResponse("company admin has been deleted with id: "+ id));
    }

    @PostMapping("/approve-new-skill/{CompanyAdminId}/{requestId}")
    public ResponseEntity<?> approveNewSkill(@PathVariable Integer CompanyAdminId, @PathVariable Integer requestId) {
        companyAdminService.approveNewSkillRequest(CompanyAdminId , requestId);
        return ResponseEntity.status(200).body(new APIResponse("skill request with id: "+ requestId+", has been approved and added to skills"));
    }

    @PostMapping("/approve-training-request/{CompanyAdminId}/{requestId}")
    public ResponseEntity<?> approveTrainingRequest(@PathVariable Integer CompanyAdminId, @PathVariable Integer requestId) {
        companyAdminService.approvedTraining(CompanyAdminId , requestId);
        return ResponseEntity.status(200).body(new APIResponse("training request with id: "+ requestId+", has been approved and added to Training"));
    }

    @PutMapping("/reject-training-request/{companyAdminId}/{requestId}")
    public ResponseEntity<?> rejectTrainingRequest(@PathVariable Integer companyAdminId , @PathVariable Integer requestId
            , @RequestBody @Validated(ValidationGroup2.class)RequestTrainingDTOIn requestTrainingDTOIn) {
        companyAdminService.rejectRequestTraining(companyAdminId, requestId, requestTrainingDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the request was rejected successfully"));
    }
}