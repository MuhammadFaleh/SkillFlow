package org.example.skillflow.DTO.Out;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSkillRequestDTOOut {

    private Integer request_id;
    private String name;
    private String description;
    private String status;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Integer checkedBy;
    private Integer employee_id;
    private Integer company_id;
}