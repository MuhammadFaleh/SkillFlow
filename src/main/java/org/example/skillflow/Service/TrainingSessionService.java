package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.TrainingSessionDTOIn;
import org.example.skillflow.DTO.Out.TrainingSessionDTOOut;
import org.example.skillflow.Model.*;
import org.example.skillflow.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingSessionService {
    private final TrainingEnrollRequestRepository requestRepository;
    private final TrainingSessionRepository sessionRepository;
    private final TrainingRepository trainingRepository;
    private final SkillsRepository skillsRepository;
    private final EmployeeRepository employeeRepository;

    public List<TrainingSessionDTOOut> getTrainingSession(){
        List<TrainingSessionDTOOut> sessionDTOOuts = new ArrayList<>();
        for(TrainingSession training : sessionRepository.findAll()){
            sessionDTOOuts.add(convertToDTO(training));
        }
        return sessionDTOOuts;
    }

    public void createSession(TrainingSessionDTOIn trainingSessionDTOIn){
        TrainingEnrollRequest request = requestRepository.findRequestById(trainingSessionDTOIn.getRequest_id());
        if(request == null || !request.getEmployee().getId().equals(trainingSessionDTOIn.getEmployee_id())){
            throw new APIException("request not found");
        }
        if(!request.getStatus().equalsIgnoreCase("approved")){
            throw new APIException("request not approved");
        }
        TrainingSession session = sessionRepository.
                findSessionByNewestTrainingIdAndEmployee(request.getTraining().getId() ,request.getEmployee().getId());
        if( session != null && !session.getStatus().equalsIgnoreCase("cancelled")
        ){
            throw new APIException("you already took that training or have it still in progress");
        }

        TrainingSession trainingSession = convertToEntity(trainingSessionDTOIn);
        trainingSession.setCompany(request.getCompany());
        trainingSession.setEmployee(request.getEmployee());
        trainingSession.setStart_date(LocalDateTime.now());
        trainingSession.setTraining(request.getTraining());
        sessionRepository.save(trainingSession);

    }

    public void updateSession(Integer id, TrainingSessionDTOIn sessionDTOIn){
        TrainingSession trainingSession = sessionRepository.findSessionById(id);

        if(trainingSession == null || !trainingSession.getEmployee().getId().equals(sessionDTOIn.getEmployee_id())){
            throw new APIException("session not found");
        }

        if(!trainingSession.getStatus().equalsIgnoreCase("in progress")){
            throw new APIException("session is closed and can't be updated");
        }

        trainingSession.setNotes(sessionDTOIn.getNotes());
        sessionRepository.save(trainingSession);
    }

    public void deleteSession(Integer id , Integer employee_id){
        TrainingSession trainingSession = sessionRepository.findSessionById(id);

        if(trainingSession == null || !trainingSession.getEmployee().getId().equals(employee_id)){
            throw new APIException("session not found");
        }

        if(!trainingSession.getStatus().equalsIgnoreCase("in progress")){
            throw new APIException("session is closed and can't be deleted");
        }

        sessionRepository.delete(trainingSession);
    }

    public void completeSession(Integer id, Integer employee_id){
        TrainingSession session = sessionRepository.findSessionById(id);

        if(session == null){
            throw new APIException("training session not found");
        }

        if(!session.getStatus().equalsIgnoreCase("in progress")){
            throw new APIException("training session already finished");
        }

        if(!session.getEmployee().getId().equals(employee_id)){
            throw new APIException("unauthorized to make changes");
        }

        if(session.getStart_date().isAfter(LocalDateTime.now())){
            throw new APIException("training session not yet started");
        }

        session.setEnd_date(LocalDateTime.now());
        session.setStatus("completed");
        Employee employee = session.getEmployee();
        Skills skills = session.getTraining().getSkills();
        employee.getSkills().add(skills);
        skills.getEmployee().add(employee);
        employeeRepository.save(employee);
        skillsRepository.save(skills);
        sessionRepository.save(session);
    }

    public void cancelSession(Integer id, Integer employee_id, TrainingSessionDTOIn sessionDTOIn){
        TrainingSession session = sessionRepository.findSessionById(id);

        if(session == null){
            throw new APIException("training session not found");
        }

        if(!session.getStatus().equalsIgnoreCase("in progress")){
            throw new APIException("training session already finished");
        }

        if(!session.getEmployee().getId().equals(employee_id)){
            throw new APIException("unauthorized to make changes");
        }

        if(session.getStart_date().isAfter(LocalDateTime.now())){
            throw new APIException("training session not yet started");
        }

        session.setEnd_date(LocalDateTime.now());
        session.setStatus("cancelled");
        session.setNotes(sessionDTOIn.getNotes());
        sessionRepository.save(session);
    }

    public List<TrainingSessionDTOOut> getTrainingSessionByEmployeeAndStatus(Integer employee_id,String status){
        List<TrainingSessionDTOOut> sessionDTOOuts = new ArrayList<>();
        for(TrainingSession training : sessionRepository.findTrainingSessionByEmployeeIdAndStatus(employee_id,status)){
            sessionDTOOuts.add(convertToDTO(training));
        }
        return sessionDTOOuts;
    }

    public TrainingSession convertToEntity(TrainingSessionDTOIn sessionDTOIn){
        return new TrainingSession(sessionDTOIn.getTrainingSession_id(),sessionDTOIn.getStart_date(),null,
                null,"in progress",null,null,null);
    }

    public TrainingSessionDTOOut convertToDTO(TrainingSession session){
        return new TrainingSessionDTOOut(session.getId(),session.getTraining().getId(),session.getTraining().getName(),
                session.getStart_date(),session.getEnd_date(),session.getStatus(),session.getTraining().getSkills().getId(),session.getEmployee().getId());
    }


}
