package org.example.skillflow.DTO.In;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainingDTOIn {

    private Integer trainingId;

    @NotBlank(message = "Name must be not empty")
    @Size(min = 5, max = 200, message = "name length must be between 5 and 200 characters long")
    private String name;

    @NotBlank(message = "description must not be empty")
    @Size(min = 5, max = 200, message = "description length must be between 5 and 200 characters long")
    private String description;

    @NotNull(message = "skills_id must be not empty")
    private Integer skills_id;

    @NotNull(message = "company_id must be not empty")
    private Integer company_id;
}
