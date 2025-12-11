package org.example.skillflow.DTO.Out;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectManagerShortDTOOut {
    private Integer projectManager_id;
    private String username;
    private String email;
}
