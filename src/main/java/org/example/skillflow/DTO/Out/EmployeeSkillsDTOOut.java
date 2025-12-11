package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.skillflow.Model.Project;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSkillsDTOOut {
    private Integer employee_id;
    private String username;
    private String full_name;
    private String email;
    private Integer manager_id;
    private Set<SkillsDTOOut> skills;

}
