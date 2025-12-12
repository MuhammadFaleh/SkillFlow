package org.example.skillflow.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIResponse;
import org.example.skillflow.DTO.In.ProjectManagerDTOIn;
import org.example.skillflow.Service.ProjectManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projectManager")
@RequiredArgsConstructor
public class ProjectManagerController {

    private final ProjectManagerService projectManagerService;

    @GetMapping("/get")
    public ResponseEntity<?> getProjectManagers() {
        return ResponseEntity.status(200).body(projectManagerService.getProjectManagers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProjectManager(@RequestBody @Valid ProjectManagerDTOIn projectManagerDTOIn) {
        projectManagerService.createProjectManager(projectManagerDTOIn);

        return ResponseEntity.status(200).body(new APIResponse("project manager has been created with id: " + projectManagerDTOIn.getProject_Manager_id()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProjectManager(@PathVariable Integer id, @RequestBody @Valid ProjectManagerDTOIn projectManagerDTOIn) {
        projectManagerService.updateProjectManager(id ,projectManagerDTOIn);

        return ResponseEntity.status(200).body(new APIResponse("project manager has been updated with id: " + id));
    }

    @DeleteMapping("/delete/{id}/{companyId}")
    public ResponseEntity<?> deleteProjectManager(@PathVariable Integer id,@PathVariable Integer companyId) {
        projectManagerService.deleteProjectManager(id , companyId);

        return ResponseEntity.status(200).body(new APIResponse("project manager has been deleted with id: " + id));
    }

    @PutMapping("/assign-project/{projectManagerId}/{projectId}/{companyId}")
    public ResponseEntity<?> assignProjectToManager(@PathVariable Integer projectManagerId, @PathVariable Integer projectId, @PathVariable Integer companyId) {
        projectManagerService.assignProjectToManager(projectManagerId, projectId,companyId);

        return ResponseEntity.status(200).body(new APIResponse("project with id: "+ projectId +", has been assigned to project manager with id: " + projectManagerId));
    }

    @PutMapping("/unassign-project/{projectManagerId}/{projectId}/{companyId}")
    public ResponseEntity<?> unassignProjectFromManager(@PathVariable Integer projectManagerId, @PathVariable Integer projectId, @PathVariable Integer companyId) {
        projectManagerService.unassignProjectFromManager(projectManagerId,projectId, companyId);

        return ResponseEntity.status(200).body(new APIResponse("project with id: "+ projectId +" has been unassigned from project manager with id: " + projectManagerId));
    }

    @GetMapping("/get-project-manager/{id}")
    public ResponseEntity<?> getProjectManagerById(@PathVariable Integer id) {

        return ResponseEntity.status(200).body(projectManagerService.getProjectManagerById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getProjectManagersByCompany(@PathVariable Integer companyId) {
        return ResponseEntity.status(200).body(projectManagerService.getProjectManagersByCompany(companyId));

    }

    @GetMapping("/company-risk-over/{companyId}/{limit}")
    public ResponseEntity<?> getProjectManagersByRiskOver(@PathVariable Integer companyId,@PathVariable Integer limit) {
        return ResponseEntity.status(200).body(projectManagerService.getProjectManagersByCompanyAndRiskOver(companyId, limit));
    }

}
