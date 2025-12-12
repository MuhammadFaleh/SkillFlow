package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.TrainingDTOIn;
import org.example.skillflow.Service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training")
public class TrainingController {
    private final TrainingService trainingService;

    @GetMapping("/get-training")
    public ResponseEntity<?> getAllTraining(){
        return ResponseEntity.status(200).body(trainingService.getTraining());
    }

    @PostMapping("/create-training")
    public ResponseEntity<?> createTraining(@RequestBody @Valid TrainingDTOIn trainingDTOIn){
        trainingService.createTraining(trainingDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the training was created successfully"));
    }

    @PutMapping("/update-training/{trainingId}")
    public ResponseEntity<?> updateTraining(@PathVariable Integer trainingId, @RequestBody @Valid TrainingDTOIn trainingDTOIn){
        trainingService.updateTraining(trainingId, trainingDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the training was updated successfully"));
    }

    @DeleteMapping("/delete-training/{trainingId}/{companyId}")
    public ResponseEntity<?> deleteTraining(@PathVariable Integer trainingId, @PathVariable Integer companyId){
        trainingService.deleteTraining(trainingId, companyId);
        return ResponseEntity.status(200).body(new APIResponse("the training was deleted successfully"));
    }

    @GetMapping("/get-training-id/{trainingId}")
    public ResponseEntity<?> getTrainingById(@PathVariable Integer trainingId){
        return ResponseEntity.status(200).body(trainingService.getTrainingById(trainingId));
    }

    @GetMapping("/get-training-company/{companyId}")
    public ResponseEntity<?> getTrainingByCompany(@PathVariable Integer companyId){
        return ResponseEntity.status(200).body(trainingService.getTrainingByCompany(companyId));
    }
}