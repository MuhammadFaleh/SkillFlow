package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.RequestTrainingDTOIn;
import org.example.skillflow.Service.RequestTrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/request-training")
@RequiredArgsConstructor
public class RequestTrainingController {

    private final RequestTrainingService requestTrainingService;


    @GetMapping("/get")
    public ResponseEntity<?> getAllRequestTraining(){
        return ResponseEntity.status(200).body(requestTrainingService.getRequestTraining());
    }

    @PostMapping("/create-request-training")
    public ResponseEntity<?> addRequestTraining(@RequestBody @Valid RequestTrainingDTOIn requestTrainingDTOIn){
        requestTrainingService.addRequestTraining(requestTrainingDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("Added request Training successfully"));
    }

    @PutMapping("/update-request-training/{requestTrainingId}")
    public ResponseEntity<?> updateRequestTraining(@PathVariable Integer requestTrainingId , @RequestBody @Valid RequestTrainingDTOIn requestTrainingDTOIn){
        requestTrainingService.updateRequestTraining(requestTrainingId, requestTrainingDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("Updated request Training successfully"));
    }

    @DeleteMapping("/delete-request-training/{requestTrainingId}")
    public ResponseEntity<?> deleteRequestTraining(@PathVariable Integer requestTrainingId){
        requestTrainingService.deleteRequestTraining(requestTrainingId);
        return ResponseEntity.status(200).body(new APIResponse("Deleted request Training successfully"));
    }
}
