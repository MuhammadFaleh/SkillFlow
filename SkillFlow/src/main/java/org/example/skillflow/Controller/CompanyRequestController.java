package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.Model.Company_Request;
import org.example.skillflow.Service.CompanyRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companyRequest")
@AllArgsConstructor
public class CompanyRequestController {


    private final CompanyRequestService companyRequestService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllCompanyRequest(){
        return ResponseEntity.status(200).body(companyRequestService.getAllCompanyRequests());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCompanyRequest(@RequestBody @Valid Company_Request companyRequest){
        companyRequestService.addCompanyRequest(companyRequest);
        return ResponseEntity.status(200).body(new APIResponse("added company request successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompanyRequest(@PathVariable Integer id , @RequestBody @Valid Company_Request companyRequest){
        companyRequestService.updateCompanyRequest(id, companyRequest);
        return ResponseEntity.status(200).body(new APIResponse("updated company request successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompanyRequest(@PathVariable Integer id){
        companyRequestService.deleteCompanyRequest(id);
        return ResponseEntity.status(200).body(new APIResponse("deleted company successfully"));
    }
}
