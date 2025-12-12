package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingEnrollRequestDTOOut {
    private Integer trainingEnroll_request_id;
    private Integer Training_id;
    private String Training_name;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Integer skills_id;
    private String status;
    private String description;
    private String notes;
    private Integer checked_by;
    private Integer employee_id;

}
