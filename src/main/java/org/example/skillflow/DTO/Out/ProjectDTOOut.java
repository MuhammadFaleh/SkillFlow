package org.example.skillflow.DTO.Out;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ProjectDTOOut {

    private Integer project_id;
    private String description;
    private String status;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private String risk;

}
