package org.example.skillflow.DTO.In;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDTOIn {
    private Integer training_id;

    @NotBlank(message = "training name must not be empty")
    @Size(min = 5, max = 200, message = "training name length must be between 5 and 200 characters long")
    private String name;

    @NotBlank(message = "training description must not be empty")
    @Size(min = 5, max = 300, message = "training description length must be between 5 and 300 characters long")
    private String description;

    private Integer company_id;

    private Integer skill_id;
}
