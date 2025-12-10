package org.example.skillflow.DTO.In;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSkillRequestDTOIn {
    private Integer request_id;

    @NotEmpty(message = "skill name cannot be empty")
    private String name;

    @NotEmpty(message = "skill description cannot be empty")
    private String description;

    @NotNull(message = "employee id cannot be null")
    private Integer employee_id;


}