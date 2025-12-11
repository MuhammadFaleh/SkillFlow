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
    private final EmailService emailService;

    @GetMapping("/get")
    public ResponseEntity<?> getCompanyAdmins() {
        return ResponseEntity.status(200).body(companyAdminService.getCompanyAdmins());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCompanyAdmin(@RequestBody @Valid CompanyAdminDTOIn companyAdminDTOIn) {
        companyAdminService.createCompanyAdmin(companyAdminDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("company admin has been created with id: "+ companyAdminDTOIn.getCompanyAdmin_id()));
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


    @PostMapping("/approve-new-skill/{CompanyAdminId}/{requestId}")
    public ResponseEntity<?> approveNewSkill(@PathVariable Integer CompanyAdminId, @PathVariable Integer requestId) {
        companyAdminService.approveNewSkillRequest(CompanyAdminId , requestId);
        return ResponseEntity.status(200).body(new APIResponse("skill request with id: "+ requestId+", has been approved and added to skills"));
    }

    @PutMapping("/reject-new-skill/{CompanyAdminId}/{requestId}")
    public ResponseEntity<?> rejectNewSkill(@PathVariable Integer CompanyAdminId, @PathVariable Integer requestId) {
        companyAdminService.rejectNewSkillRequest(CompanyAdminId, requestId);
        //emailService.sendEmail( , "Request rejected", "Your request with id: "+requestId+ ", has been rejected");
        return ResponseEntity.status(200).body(new APIResponse("skill request with id: "+ requestId+", has been rejected"));
    }
}