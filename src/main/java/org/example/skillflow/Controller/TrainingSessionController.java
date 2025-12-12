package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.TrainingSessionDTOIn;
import org.example.skillflow.Service.TrainingSessionService;
import org.example.skillflow.vaildationGroups.ValidationGroup1;
import org.example.skillflow.vaildationGroups.ValidationGroup2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training-session")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;

    @GetMapping("/get-all-training-sessions")
    public ResponseEntity<?> getAllTrainingSessions(){
        return ResponseEntity.status(200).body(trainingSessionService.getTrainingSession());
    }

    @PostMapping("/create-training-session")
    public ResponseEntity<?> createSession(@RequestBody @Validated(ValidationGroup1.class) TrainingSessionDTOIn trainingSessionDTOIn){
        trainingSessionService.createSession(trainingSessionDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the training session was created successfully"));
    }

    @PutMapping("/update-training-session/{sessionId}")
    public ResponseEntity<?> updateSession(@PathVariable Integer sessionId, @RequestBody @Validated(ValidationGroup1.class) TrainingSessionDTOIn trainingSessionDTOIn){
        trainingSessionService.updateSession(sessionId, trainingSessionDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the training session was updated successfully"));
    }

    @DeleteMapping("/delete-training-session/{sessionId}/{employeeId}")
    public ResponseEntity<?> deleteSession(@PathVariable Integer sessionId,@PathVariable Integer employeeId){
        trainingSessionService.deleteSession(sessionId, employeeId);
        return ResponseEntity.status(200).body(new APIResponse("the training session was deleted successfully"));
    }

    @PutMapping("/complete-training-session/{sessionId}/{employeeId}")
    public ResponseEntity<?> completeSession(@PathVariable Integer sessionId, @PathVariable Integer employeeId){
        trainingSessionService.completeSession(sessionId, employeeId);
        return ResponseEntity.status(200).body(new APIResponse("the training session was completed successfully"));
    }

    @PutMapping("/cancel-training-session/{sessionId}/{employeeId}")
    public ResponseEntity<?> cancelSession(@PathVariable Integer sessionId, @PathVariable Integer employeeId,
                                           @RequestBody @Validated(ValidationGroup2.class) TrainingSessionDTOIn trainingSessionDTOIn){
        trainingSessionService.cancelSession(sessionId, employeeId, trainingSessionDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the training session was canceled successfully"));
    }

    @GetMapping("/get-training-sessions-employee/{employeeId}/{status}")
    public ResponseEntity<?> getTrainingSessionByEmployeeAndStatus(@PathVariable Integer employeeId, @PathVariable String status){
        return ResponseEntity.status(200).body(trainingSessionService.getTrainingSessionByEmployeeAndStatus(employeeId, status));
    }
}