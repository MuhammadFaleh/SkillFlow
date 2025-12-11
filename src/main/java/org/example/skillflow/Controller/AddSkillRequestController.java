package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.AddSkillRequestDTOIn;
import org.example.skillflow.Service.AddSkillRequestService;
import org.example.skillflow.vaildationGroups.ValidationGroup1;
import org.example.skillflow.vaildationGroups.ValidationGroup2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/add-skill")
public class AddSkillRequestController {
    private final AddSkillRequestService addSkillRequestService;

    @GetMapping("/get-all-skill-requests")
    public ResponseEntity<?> getAllRequests(){
        return ResponseEntity.status(200).body(addSkillRequestService.getRequests());
    }

    @PostMapping("/create-skill-request")
    public ResponseEntity<?> createRequest(@RequestBody @Validated(ValidationGroup1.class) AddSkillRequestDTOIn addSkillRequestDTOIn){
        addSkillRequestService.createRequest(addSkillRequestDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the request was created successfully"));
    }

    @PutMapping("/update-skill-request/{requestId}")
    public ResponseEntity<?> updateRequest(@PathVariable Integer requestId,@RequestBody @Validated(ValidationGroup1.class) AddSkillRequestDTOIn addSkillRequestDTOIn){
        addSkillRequestService.updateRequest(requestId,addSkillRequestDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the request was updated successfully"));
    }

    @DeleteMapping("/delete-skill-request/{requestId}/{employeeId}")
    public ResponseEntity<?> deleteRequest(@PathVariable Integer requestId,@PathVariable Integer employeeId){
        addSkillRequestService.deleteRequest(requestId,employeeId);
        return ResponseEntity.status(200).body(new APIResponse("the request was deleted successfully"));
    }
//    approveSkill(Integer id, Integer manager_id)
    @PutMapping("/approve-skill-request/{requestId}/{managerId}")
    public ResponseEntity<?> approveRequest(@PathVariable Integer requestId,@PathVariable Integer managerId){
        addSkillRequestService.approveSkill(requestId,managerId);
        return ResponseEntity.status(200).body(new APIResponse("the request was approved successfully"));
    }

    @PutMapping("/reject-skill-request/{requestId}/{managerId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer requestId,@PathVariable Integer managerId,
                                           @RequestBody @Validated(ValidationGroup2.class) AddSkillRequestDTOIn addSkillRequestDTOIn){
        addSkillRequestService.rejectSkill(requestId,managerId, addSkillRequestDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the request was rejected successfully"));
    }

    @GetMapping("/get-skill-requests-employee/{employeeId}")
    public ResponseEntity<?> getRequestsByEmployeeId(@PathVariable Integer employeeId){

        return ResponseEntity.status(200).body(addSkillRequestService.getRequestsByEmployeeId(employeeId));
    }

    @GetMapping("/get-skill-requests-company/{companyId}")
    public ResponseEntity<?> getRequestsByCompanyId(@PathVariable Integer companyId){

        return ResponseEntity.status(200).body(addSkillRequestService.getRequestsByCompanyId(companyId));
    }

    @GetMapping("/get-skill-requests-manager/{managerId}/{status}")
    public ResponseEntity<?> getRequestsByManagerId(@PathVariable Integer managerId, @PathVariable String status){
        return ResponseEntity.status(200).body(addSkillRequestService.getRequestsByManagerIdAndStatus(managerId,status));
    }
}
