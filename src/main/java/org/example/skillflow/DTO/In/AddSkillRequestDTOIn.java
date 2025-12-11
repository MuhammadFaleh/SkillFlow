package org.example.skillflow.DTO.In;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.skillflow.vaildationGroups.ValidationGroup1;
import org.example.skillflow.vaildationGroups.ValidationGroup2;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AddSkillRequestDTOIn {
    private Integer addSkillRequest_id;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    @NotBlank(message = "please enter a description or prove of the skill", groups = ValidationGroup1.class)
    @Size(max = 300, message = "max length of the description is 300 characters")
    private String description;
    @NotBlank(message = "please enter a notes to point the reason of the rejection", groups = ValidationGroup2.class)
    @Size(max = 300, message = "max length of the notes is 300 characters")
    private String notes;
    @NotNull(message = "skill id must not be null",groups = ValidationGroup1.class)
    private Integer skill_id;
    @NotNull(message = "employee id must not be null",groups = ValidationGroup1.class)
    private Integer employee_id;


}
