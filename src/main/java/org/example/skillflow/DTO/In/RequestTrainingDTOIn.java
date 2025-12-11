package org.example.skillflow.DTO.In;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.skillflow.vaildationGroups.ValidationGroup1;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTrainingDTOIn {

    private Integer requestTrainingId;

    @NotBlank(message = "name must not be empty")
    @Size(min = 5, max = 200, message = "name length must be between 5 and 200 characters long")
    private String name;

    @NotBlank(message = "notes must not be empty")
    @Size(min = 5, max = 200, message = "notes length must be between 5 and 200 characters long")
    private String notes;

    @NotNull(message = "employee id must not be null",groups = ValidationGroup1.class)
    private Integer employee_id;

    @NotNull(message = "employee id must not be null",groups = ValidationGroup1.class)
    private Integer skill_id;

}
