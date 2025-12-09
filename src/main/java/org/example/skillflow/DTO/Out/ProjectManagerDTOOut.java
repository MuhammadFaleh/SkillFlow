package org.example.skillflow.DTO.Out;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProjectManagerDTOOut {
    private Integer projectManager_id;
    private String username;
    private String password;
    private String full_name;
    private String gender;
    private Integer age;
    private String email;
    private String risk_load;
    private Integer company_id;
}

