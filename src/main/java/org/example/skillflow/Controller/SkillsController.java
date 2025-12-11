package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.SkillsDTOIn;
import org.example.skillflow.Service.SkillsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillsController {

    private final SkillsService skillsService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllSkills(){
        return ResponseEntity.status(200).body(skillsService.getSkills());
    }

    @PostMapping("/add")
    public ResponseEntity<?> createSkills(@RequestBody @Valid SkillsDTOIn skillsDTOIn){
        skillsService.createSkills(skillsDTOIn);
        return ResponseEntity.status(200).body(new APIResponse("created skills successfully"));
    }
}
