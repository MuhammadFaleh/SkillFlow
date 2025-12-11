package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSkillRequestDTOOut {

    private Integer request_id;
    private String name;
    private String description;
    private String status;
}