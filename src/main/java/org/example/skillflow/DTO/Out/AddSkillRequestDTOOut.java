package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AddSkillRequestDTOOut {
    private Integer addSkillRequest_id;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Integer employee_id;
    private Integer skill_id;
    private String skill_name;
    private String status;
    private String description;
    private String notes;
    private Integer checked_by;
}
