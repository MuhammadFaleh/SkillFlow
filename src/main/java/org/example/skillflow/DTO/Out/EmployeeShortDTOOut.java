package org.example.skillflow.DTO.Out;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeShortDTOOut {
    private Integer employee_id;
    private String username;
    private String email;
}
