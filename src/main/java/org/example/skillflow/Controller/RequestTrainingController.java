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

    @PostMapping("/add")
    public ResponseEntity<?> addRequestTraining(@RequestBody @Valid RequestTrainingDTOIn requestTrainingDTOIn){
        requestTrainingService.addRequestTraining(requestTrainingDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("Added request Training successfully"));
    }

}
