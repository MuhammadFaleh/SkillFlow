package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.CompanyAdminDTOIn;
import org.example.skillflow.Service.CompanyAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company-admin")
@RequiredArgsConstructor
public class CompanyAdmin {

    private final CompanyAdminService companyAdminService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllCompanyAdmin(){
        return ResponseEntity.status(200).body(companyAdminService.getCompanyAdmin());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCompanyAdmin(@RequestBody @Valid CompanyAdminDTOIn companyAdminDTOIn){
        companyAdminService.addCompanyAdmin(companyAdminDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("Added company admin successfully"));
    }

    @PutMapping("/update/{companyAdminId}")
    public ResponseEntity<?> updateCompanyAdmin(@PathVariable Integer companyAdminId , @RequestBody @Valid CompanyAdminDTOIn companyAdminDTOIn){
        companyAdminService.updateCompanyAdmin(companyAdminId, companyAdminDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("Updated company admin successfully"));
    }

    @DeleteMapping("/delete/{companyAdminId}")
    public ResponseEntity<?> deleteCompanyAdmin(@PathVariable Integer companyAdminId){
        companyAdminService.deleteAdminCompany(companyAdminId);
        return ResponseEntity.status(200).body(new APIResponse("Deleted company admin successfully"));
    }
}

