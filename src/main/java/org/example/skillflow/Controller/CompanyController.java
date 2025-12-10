package org.example.skillflow.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.CompanyDTOIn;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/get")
    public ResponseEntity<?> getCompany(){
        return ResponseEntity.status(200).body(companyService.getCompany());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCompany(@RequestBody @Valid CompanyDTOIn company){
        companyService.addCompany(company);
        return ResponseEntity.status(200).body(new APIResponse("added company successfully"));
    }

    @PutMapping("/update/{companyId}")
    public ResponseEntity<?> updateCompany(@PathVariable Integer companyId , @RequestBody @Valid CompanyDTOIn company){
        companyService.updateCompany(companyId, company);
        return ResponseEntity.status(200).body(new APIResponse("updated company successfully"));
    }

    @DeleteMapping("/delete/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable Integer companyId){
        companyService.deleteCompany(companyId);
        return ResponseEntity.status(200).body(new APIResponse("deleted company successfully"));
    }
}