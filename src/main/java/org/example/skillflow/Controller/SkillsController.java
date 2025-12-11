package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.SkillsDTOIn;
import org.example.skillflow.Service.SkillsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/skills")
public class SkillsController {
    private final SkillsService skillsService;

    @GetMapping("/get-all-skills")
    public ResponseEntity<?> getAllSkills(){
        return ResponseEntity.status(200).body(skillsService.getSkills());
    }

    @PostMapping("/create-skills")
    public ResponseEntity<?> createSkills(@RequestBody @Valid SkillsDTOIn skillsDTOIn){
        skillsService.createSkills(skillsDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the skills was created successfully"));
    }

    @PutMapping("/update-skills/{skillsId}")
    public ResponseEntity<?> updateSkills(@PathVariable Integer skillsId, @RequestBody @Valid SkillsDTOIn skillsDTOIn){
        skillsService.updateSkills(skillsId, skillsDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("the skills was updated successfully"));
    }

    @DeleteMapping("/delete-skills/{skillsId}/{companyId}")
    public ResponseEntity<?> deleteSkills(@PathVariable Integer skillsId, @PathVariable Integer companyId){
        skillsService.deleteSkills(skillsId, companyId);
        return ResponseEntity.status(200).body(new APIResponse("the skills was deleted successfully"));
    }

    @GetMapping("/get-skills-company/{id}")
    public ResponseEntity<?> getSkillsByCompany(@PathVariable Integer id){
        return ResponseEntity.status(200).body(skillsService.getSkillsByCompany(id));
    }
}