package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.SkillsDTOIn;
import org.example.skillflow.DTO.In.TrainingDTOIn;
import org.example.skillflow.DTO.Out.SkillsDTOOut;
import org.example.skillflow.DTO.Out.TrainingDTOOut;
import org.example.skillflow.Model.*;
import org.example.skillflow.Repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final SkillsRepository skillsRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingEnrollRequestRepository requestRepository;

    public List<TrainingDTOOut> getTraining(){
        List<TrainingDTOOut> trainingDTOOuts = new ArrayList<>();
        for(Training training : trainingRepository.findAll()){
            trainingDTOOuts.add(convertToDTO(training));
        }
        return trainingDTOOuts;
    }

    public void createTraining(TrainingDTOIn trainingDTOIn){
        Skills skills = skillsRepository.findSkillsById(trainingDTOIn.getSkill_id());
        Training training = trainingRepository.findTrainingByCompanyIdAndName(trainingDTOIn.getCompany_id(),trainingDTOIn.getName());
        if(skills == null || !skills.getCompany().getId().equals(trainingDTOIn.getCompany_id())){
            throw new APIException("skill not found");
        }

        if(training != null){
            throw new APIException("training exist");
        }
        Training newTraining = convertToEntity(trainingDTOIn);
        newTraining.setCompany(skills.getCompany());
        newTraining.setSkills(skills);
        skills.getTraining().add(newTraining);
        skillsRepository.save(skills);
        trainingRepository.save(newTraining);
    }

    public void updateTraining(Integer id, TrainingDTOIn trainingDTOIn){
        Training training = trainingRepository.findTrainingById(id);


        if(training == null || !training.getCompany().getId().equals(trainingDTOIn.getCompany_id())){
            throw new APIException("training not found");
        }

        Training t = trainingRepository.findTrainingByCompanyIdAndName(trainingDTOIn.getCompany_id(), trainingDTOIn.getName());

        if(t != null && !t.getName().equalsIgnoreCase(training.getName())){
            throw new APIException("training already exist");
        }
        training.setDescription(trainingDTOIn.getDescription());
        training.setName(trainingDTOIn.getName());
        trainingRepository.save(training);
    }

    public void deleteTraining(Integer id,Integer company_id){
        Training training = trainingRepository.findTrainingById(id);


        if(training == null || !training.getCompany().getId().equals(company_id)){
            throw new APIException("training not found");
        }

        trainingSessionRepository.deleteByTrainingId(training.getId());
        requestRepository.deleteByTrainingId(training.getId());
        trainingRepository.delete(training);
    }

    public TrainingDTOOut getTrainingById(Integer id){
        return convertToDTO(trainingRepository.findTrainingById(id));
    }

    public List<TrainingDTOOut> getTrainingByCompany(Integer id){
        List<TrainingDTOOut> trainingDTOOuts = new ArrayList<>();
        for(Training training : trainingRepository.findTrainingByCompanyId(id)){
            trainingDTOOuts.add(convertToDTO(training));
        }
        return trainingDTOOuts;
    }

    public Training convertToEntity(TrainingDTOIn trainingDTOIn){
        return new Training(null,trainingDTOIn.getName(),trainingDTOIn.getDescription(),
                null,null,null);
    }

    public TrainingDTOOut convertToDTO(Training training){
        return new TrainingDTOOut(training.getId(),training.getName(),training.getDescription(),
                new SkillsDTOOut(training.getSkills().getId(),training.getSkills().getName(),training.getSkills().getDescription(),training.getCompany().getId()));
    }
}
