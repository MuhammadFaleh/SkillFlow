package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Model.Skills;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFullDTOOut {
    private Integer project_id;
    private String description;
    private String status;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private String risk;
    private Set<ProjectManagerShortDTOOut> projectManager;
    private Set<EmployeeShortDTOOut> Employee;
    private Set<SkillsDTOOut> skills;
}
