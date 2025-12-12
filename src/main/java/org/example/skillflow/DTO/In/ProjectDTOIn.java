package org.example.skillflow.DTO.In;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.skillflow.Model.Employee;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTOIn {

    private Integer project_id;

    @NotNull(message = "company id cannot be null")
    private Integer company_Id;

    @NotEmpty(message = "description cannot be empty")
    private String description;

    @NotNull(message = "start date cannot be null")
    private LocalDateTime start_date;

    @NotNull(message = "end date cannot be null")
    private LocalDateTime end_date;

    @Pattern(regexp = "(?i)^(low|medium|high|critical)$")
    @NotNull(message = "risk cannot be null")
    private String risk;


}
