package org.example.skillflow.DTO.In.AI;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainRecommendDTOIn {

    @NotNull(message = "company id must not be null")
    private Integer company_id;
    @NotNull(message = "employee id must not be null")
    private Integer employee_id;
}
