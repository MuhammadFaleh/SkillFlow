package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.DTO.In.TrainingDTOIn;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.Training;
import org.example.skillflow.Repository.TrainingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    public List<Training> getTrainings(){
        return trainingRepository.findAll();
    }

    public void addSkills(TrainingDTOIn trainingDTOIn){

    }

    public Training convertToEntity(TrainingDTOIn trainingDTOIn){
        return new Training(trainingDTOIn.getTrainingId() , trainingDTOIn.getName() , trainingDTOIn.getDescription() , null , null );
    }
}
