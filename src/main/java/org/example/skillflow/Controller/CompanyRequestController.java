package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.skillflow.API.APIResponse;
<<<<<<< HEAD
import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Service.CompanyRequestService;
=======
import org.example.skillflow.DTO.In.CompanyRequestDTOIn;
import org.example.skillflow.DTO.In.CreateUserCompanyDTO;
import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Service.CompanyRequestService;
import org.example.skillflow.Service.CompanyService;
>>>>>>> test
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companyRequest")
@AllArgsConstructor
public class CompanyRequestController {


    private final CompanyRequestService companyRequestService;
<<<<<<< HEAD
=======
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
>>>>>>> test

    @GetMapping("/get")
    public ResponseEntity<?> getAllCompanyRequest(){
        return ResponseEntity.status(200).body(companyRequestService.getAllCompanyRequests());
    }

    @PostMapping("/add")
<<<<<<< HEAD
    public ResponseEntity<?> addCompanyRequest(@RequestBody @Valid CompanyRequest companyRequest){
=======
    public ResponseEntity<?> addCompanyRequest(@RequestBody @Valid CompanyRequestDTOIn companyRequest){
>>>>>>> test
        companyRequestService.addCompanyRequest(companyRequest);
        return ResponseEntity.status(200).body(new APIResponse("added company request successfully"));
    }

    @PutMapping("/update/{id}")
<<<<<<< HEAD
    public ResponseEntity<?> updateCompanyRequest(@PathVariable Integer id , @RequestBody @Valid CompanyRequest companyRequest){
=======
    public ResponseEntity<?> updateCompanyRequest(@PathVariable Integer id , @RequestBody @Valid CompanyRequestDTOIn companyRequest){
>>>>>>> test
        companyRequestService.updateCompanyRequest(id, companyRequest);
        return ResponseEntity.status(200).body(new APIResponse("updated company request successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompanyRequest(@PathVariable Integer id){
        companyRequestService.deleteCompanyRequest(id);
        return ResponseEntity.status(200).body(new APIResponse("deleted company successfully"));
    }
<<<<<<< HEAD
=======

    @GetMapping("/checkStatusRequest/{recordNumber}")
    public ResponseEntity<?> getRequestStatus(@PathVariable String recordNumber){
        return ResponseEntity.status(200).body(companyRequestService.checkStatusOrderByRecordNumber(recordNumber));
    }

    @PostMapping("/createUser/{recordNumber}")
    public ResponseEntity<?> createUser(@PathVariable String recordNumber , @RequestBody @Valid CreateUserCompanyDTO createUserCompanyDTO){
        companyService.createUserCompany(recordNumber, createUserCompanyDTO);
        return ResponseEntity.status(200).body(new APIResponse("Created account successfully"));
    }
>>>>>>> test
}
