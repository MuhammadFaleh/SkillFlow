package org.example.skillflow.DTO.In.AI;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillRecommendDTOIn {
    @NotNull(message = "company id must not be null")
    private Integer company_id;
    @NotNull(message = "project id must not be null")
    private Integer project_id;
}
