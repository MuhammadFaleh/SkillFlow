package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.ProjectDTOIn;
import org.example.skillflow.Service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get")
    public ResponseEntity<?> getProjects() {
        return ResponseEntity.status(200).body(projectService.getProjects());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody @Valid ProjectDTOIn projectDTOIn) {
        projectService.createProject(projectDTOIn);

        return ResponseEntity.status(200).body(new APIResponse("project created successfully with id: " + projectDTOIn.getProject_id()));
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Integer projectId, @RequestBody @Valid ProjectDTOIn projectDTOIn) {
        projectService.updateProject(projectId,projectDTOIn);

        return ResponseEntity.status(200).body(new APIResponse("project updated successfully with id: " + projectId));
    }

    @DeleteMapping("/delete/{projectId}/{companyId}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer projectId, @PathVariable Integer companyId) {
        projectService.deleteProject(projectId,companyId);

        return ResponseEntity.status(200).body(new APIResponse("project deleted successfully with id: " + projectId +", in company with id: " + companyId ));
    }

    @PutMapping("/assign-skill/{projectId}/{skillId}/{companyId}")
    public ResponseEntity<?> assignSkillToProject(@PathVariable Integer projectId, @PathVariable Integer skillId, @PathVariable Integer companyId) {
        projectService.assignSkillToProject(projectId ,skillId,companyId);

        return ResponseEntity.status(200).body(new APIResponse("skill with id: "+ skillId+", has been added to project with id: "+ projectId));
    }


    @PutMapping("/unassign-skill/{projectId}/{skillId}/{companyId}")
    public ResponseEntity<?> unassignSkillFromProject(@PathVariable Integer projectId, @PathVariable Integer skillId, @PathVariable Integer companyId) {
        projectService.unassignSkillFromProject(projectId,skillId, companyId);
        return ResponseEntity.status(200).body(new APIResponse("skill with id: "+ skillId+", unassigned from project with id: " + projectId));
    }

}
