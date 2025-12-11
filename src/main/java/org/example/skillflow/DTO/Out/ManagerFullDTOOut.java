package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerFullDTOOut {
    private Integer manager_id;
    private String username;
    private String full_name;
    private String gender;
    private Integer age;
    private String email;
    private Integer company_id;
    private Set<EmployeeShortDTOOut> Employee;
}
