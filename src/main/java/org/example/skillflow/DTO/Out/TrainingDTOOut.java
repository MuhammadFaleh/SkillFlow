package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.skillflow.Model.Skills;
import org.example.skillflow.Model.Training;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDTOOut {
    private Integer training_id;

    private String name;

    private String description;

    private SkillsDTOOut skills;

}
