package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.CompanyRequestDTOIn;
import org.example.skillflow.DTO.In.CreateUserCompanyDTO;
import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Service.CompanyRequestService;
import org.example.skillflow.Service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company-request")
@AllArgsConstructor
public class CompanyRequestController {


    private final CompanyRequestService companyRequestService;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllCompanyRequest(){
        return ResponseEntity.status(200).body(companyRequestService.getAllCompanyRequests());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCompanyRequest(@RequestBody @Valid CompanyRequestDTOIn companyRequest){
        companyRequestService.addCompanyRequest(companyRequest);
        return ResponseEntity.status(200).body(new APIResponse("added company request successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompanyRequest(@PathVariable Integer id , @RequestBody @Valid CompanyRequestDTOIn companyRequest){
        companyRequestService.updateCompanyRequest(id, companyRequest);
        return ResponseEntity.status(200).body(new APIResponse("updated company request successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompanyRequest(@PathVariable Integer id){
        companyRequestService.deleteCompanyRequest(id);
        return ResponseEntity.status(200).body(new APIResponse("deleted company successfully"));
    }

    @GetMapping("/checkStatusRequest/{recordNumber}")
    public ResponseEntity<?> getRequestStatus(@PathVariable String recordNumber){
        return ResponseEntity.status(200).body(companyRequestService.checkStatusOrderByRecordNumber(recordNumber));
    }

    @PostMapping("/createUser/{recordNumber}")
    public ResponseEntity<?> createUser(@PathVariable String recordNumber , @RequestBody @Valid CreateUserCompanyDTO createUserCompanyDTO){
        companyService.createUserCompany(recordNumber, createUserCompanyDTO);
        return ResponseEntity.status(200).body(new APIResponse("Created account successfully"));
    }
}