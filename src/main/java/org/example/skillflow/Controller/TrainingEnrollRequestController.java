package org.example.skillflow.Controller;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.TrainingEnrollRequestDTOIn;
import org.example.skillflow.Service.TrainingEnrollRequestService;
import org.example.skillflow.vaildationGroups.ValidationGroup1;
import org.example.skillflow.vaildationGroups.ValidationGroup2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training-enroll-request")
public class TrainingEnrollRequestController {
    private final TrainingEnrollRequestService trainingEnrollRequestService;

    @GetMapping("/get-training-requests")
    public ResponseEntity<?> getAllRequests(){
        return ResponseEntity.status(200).body(trainingEnrollRequestService.getRequests());
    }

    @PostMapping("/create-training-request")
    public ResponseEntity<?> createRequest(@RequestBody @Validated(ValidationGroup1.class) TrainingEnrollRequestDTOIn trainingEnrollRequestDTOIn){
        trainingEnrollRequestService.createRequest(trainingEnrollRequestDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the request was created successfully"));
    }

    @PutMapping("/update-training-request/{requestId}")
    public ResponseEntity<?> updateRequest(@PathVariable Integer requestId, @RequestBody @Validated(ValidationGroup1.class) TrainingEnrollRequestDTOIn trainingEnrollRequestDTOIn){
        trainingEnrollRequestService.updateRequest(requestId, trainingEnrollRequestDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the request was updated successfully"));
    }

    @DeleteMapping("/delete-training-request/{requestId}/{employeeId}")
    public ResponseEntity<?> deleteRequest(@PathVariable Integer requestId, @PathVariable Integer employeeId){
        trainingEnrollRequestService.deleteRequest(requestId, employeeId);
        return ResponseEntity.status(200).body(new APIResponse("the request was deleted successfully"));
    }

    @PutMapping("/approve-training-request/{requestId}/{managerId}")
    public ResponseEntity<?> approveRequest(@PathVariable Integer requestId, @PathVariable Integer managerId){
        trainingEnrollRequestService.approveRequest(requestId, managerId);
        return ResponseEntity.status(200).body(new APIResponse("the request was approved successfully"));
    }

    @PutMapping("/reject-training-request/{requestId}/{managerId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer requestId, @PathVariable Integer managerId,
                                           @RequestBody @Validated(ValidationGroup2.class) TrainingEnrollRequestDTOIn trainingEnrollRequestDTOIn){
        trainingEnrollRequestService.rejectRequest(requestId, managerId, trainingEnrollRequestDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the request was rejected successfully"));
    }

    @GetMapping("/get-training-requests-employee/{employeeId}/{status}")
    public ResponseEntity<?> getRequestsByEmployeeIdAndStatus(@PathVariable Integer employeeId, @PathVariable String status){
        return ResponseEntity.status(200).body(trainingEnrollRequestService.getRequestsByEmployeeIdAndStatus(employeeId, status));
    }

    @GetMapping("/get-training-requests-manager/{managerId}/{status}")
    public ResponseEntity<?> getRequestsByManagerIdAndStatus(@PathVariable Integer managerId, @PathVariable String status){
        return ResponseEntity.status(200).body(trainingEnrollRequestService.getRequestsByManagerIdAndStatus(managerId, status));
    }

    @GetMapping("/get-training-requests-company/{companyId}")
    public ResponseEntity<?> getRequestsByCompanyId(@PathVariable Integer companyId){
        return ResponseEntity.status(200).body(trainingEnrollRequestService.getRequestsByCompanyId(companyId));
    }
}