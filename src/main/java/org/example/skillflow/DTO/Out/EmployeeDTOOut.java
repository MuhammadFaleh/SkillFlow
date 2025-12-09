package org.example.skillflow.DTO.Out;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTOOut {
    private Integer employee_id;
    private String username;
    private String full_name;
    private String gender;
    private Integer age;
    private String email;
}
