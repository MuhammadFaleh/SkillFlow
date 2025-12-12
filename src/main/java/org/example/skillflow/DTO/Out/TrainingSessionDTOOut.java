package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSessionDTOOut {
    private Integer session_id;
    private Integer training_id;
    private String training_name;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private String status;
    private Integer skills_id;
    private Integer employee_id;

}
