package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.NewSkillRequestDTOIn;
import org.example.skillflow.Service.NewSkillRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/newSkillRequest")
@RequiredArgsConstructor
public class NewSkillRequestController {

    private final NewSkillRequestService newSkillRequestService;

    @GetMapping("/get")
    public ResponseEntity<?> getNewSkillRequests() {
        return ResponseEntity.status(200).body(newSkillRequestService.getNewSkillRequests());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewSkillRequest(@RequestBody @Valid NewSkillRequestDTOIn newSkillRequestDTOIn) {
        newSkillRequestService.createNewSkillRequest(newSkillRequestDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("new skill request has been created with id: " + newSkillRequestDTOIn.getRequest_id()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateNewSkillRequest(@PathVariable Integer id,@RequestBody @Valid NewSkillRequestDTOIn newSkillRequestDTOIn) {
        newSkillRequestService.updateNewSkillRequest(id, newSkillRequestDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("new skill request has been updated with id: "+ id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNewSkillRequest(@PathVariable Integer id) {
        newSkillRequestService.deleteNewSkillRequest(id);
        return ResponseEntity.status(200).body(new APIResponse("new skill request has been deleted with id: " + id));
    }
}
