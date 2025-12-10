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
public class SkillsDTOIn {
    private Integer skills_id;

    @NotBlank(message = "skill name must not be empty")
    @Size(min = 5, max = 200, message = "skill name length must be between 5 and 200 characters long")
    private String name;

    @NotBlank(message = "skill description must not be empty")
    @Size(min = 5, max = 300, message = "skill description length must be between 5 and 300 characters long")
    private String description;

    private Integer company_id;
}
