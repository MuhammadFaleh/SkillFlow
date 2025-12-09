package org.example.skillflow.DTO.Out;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillsDTOOut {
    private Integer skills_id;

    private String name;

    private String description;

    private Integer company_id;
}
