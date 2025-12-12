package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.TrainingEnrollRequestDTOIn;
import org.example.skillflow.DTO.Out.TrainingEnrollRequestDTOOut;
import org.example.skillflow.Model.*;
import org.example.skillflow.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingEnrollRequestService {
    private final TrainingEnrollRequestRepository trainingEnrollRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final SkillsRepository skillsRepository;
    private final TrainingRepository trainingRepository;
    private final CompanyRepository companyRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    public List<TrainingEnrollRequestDTOOut> getRequests(){
        List<TrainingEnrollRequestDTOOut> requestDTOOuts = new ArrayList<>();
        for(TrainingEnrollRequest request : trainingEnrollRequestRepository.findAll()){
            requestDTOOuts.add(convertToDTO(request));
        }
        return requestDTOOuts;
    }

    public void createRequest(TrainingEnrollRequestDTOIn trainingEnrollRequestDTOIn){
        TrainingEnrollRequest trainingEnrollRequest =
                trainingEnrollRequestRepository.findNewestRequestByIdAndTrainingId(trainingEnrollRequestDTOIn.getEmployee_id(),trainingEnrollRequestDTOIn.getTraining_id());

        if(trainingEnrollRequest != null){
            if(trainingEnrollRequest.getStatus().equalsIgnoreCase("pending")) {
                throw new APIException("request not checked yet you can update it");
            }
            if(trainingEnrollRequest.getStatus().equalsIgnoreCase("approved")){
                throw new APIException("request is already approved");
            }
        }
        Employee employee = employeeRepository.findEmployeeById(trainingEnrollRequestDTOIn.getEmployee_id());
        Training training = trainingRepository.findTrainingById(trainingEnrollRequestDTOIn.getTraining_id());
        if(employee == null || training == null || !training.getCompany().getId().equals(employee.getCompany().getId())){
            throw new APIException("employee or training course not found");
        }
        if(employee.getSkills().stream().anyMatch(e-> e.getId().equals(training.getSkills().getId()))){
            throw new APIException("you already have that skill");
        }

        if(!trainingEnrollRequestRepository.findTrainingRequestByEmployeeIdAndTrainingId(employee.getId(), training.getId()).isEmpty()){
            throw new APIException("you requested that training");
        }

        if(employee.getManager() == null){
            throw new APIException("employee doesn't have a manager");
        }
        TrainingEnrollRequest request = convertToEntity(trainingEnrollRequestDTOIn);
        request.setCompany(employee.getCompany());
        request.setStart_date(LocalDateTime.now());
        request.setStatus("pending");
        request.setTraining(training);
        request.setEmployee(employee);
        request.setManager(employee.getManager());
        trainingEnrollRequestRepository.save(request);
    }

    public void updateRequest(Integer id, TrainingEnrollRequestDTOIn requestDTOIn){
        TrainingEnrollRequest request = trainingEnrollRequestRepository.findRequestById(id);

        if(request == null){
            throw new APIException("no request found please make one");
        }

        if(!request.getEmployee().getId().equals(requestDTOIn.getEmployee_id())){
            throw new APIException("unauthorized to update the request");
        }

        if(!request.getStatus().equalsIgnoreCase("pending")){
            throw new APIException("unable to update request, it's already checked");
        }

        request.setDescription(request.getDescription());
        trainingEnrollRequestRepository.save(request);
    }

    public void deleteRequest(Integer id, Integer employee_id){
        TrainingEnrollRequest request =
                trainingEnrollRequestRepository.findRequestById(id);
        Employee employee = employeeRepository.findEmployeeById(employee_id);

        if(request == null || employee==null){
            throw new APIException("no request found please make one");
        }

        if(!employee_id.equals(request.getEmployee().getId())){
            throw new APIException("unauthorized to delete the request");
        }

        if(!request.getStatus().equalsIgnoreCase("pending")){
            throw new APIException("unable to delete a request, it's already checked");
        }

        trainingEnrollRequestRepository.delete(request);
    }

    public void approveRequest(Integer id, Integer manager_id){
        TrainingEnrollRequest request = trainingEnrollRequestRepository.findRequestById(id);

        if(request == null){
            throw new APIException("no request found please make one");
        }

        if(request.getManager() == null ||
                !manager_id.equals(request.getManager().getId())){
            throw new APIException("manager id doesn't match the employee manager id");
        }

        if(!request.getStatus().equalsIgnoreCase("pending")){
            throw new APIException("request is already checked");
        }
        Training training = request.getTraining();


        // send email or whatsapp message
        Employee employee = request.getEmployee();
        employeeRepository.save(employee);
        request.setStatus("approved");
        request.setEnd_date(LocalDateTime.now());
        trainingEnrollRequestRepository.save(request);
    }

    public void rejectRequest(Integer id, Integer manager_id, TrainingEnrollRequestDTOIn requestDTOIn){
        TrainingEnrollRequest request = trainingEnrollRequestRepository.findRequestById(id);

        if(request == null){
            throw new APIException("no request found please make one");
        }

        if(request.getManager() == null ||
                !manager_id.equals(request.getManager().getId())){
            throw new APIException("manager id doesn't match the employee manager id");
        }

        if(!request.getStatus().equalsIgnoreCase("pending")){
            throw new APIException("request is already checked");
        }
        // send email or whatsapp message
        request.setStatus("rejected");
        request.setEnd_date(LocalDateTime.now());
        request.setNotes(requestDTOIn.getNotes());
        trainingEnrollRequestRepository.save(request);
    }

    public List<TrainingEnrollRequestDTOOut> getRequestsByEmployeeIdAndStatus(Integer id,String status){
        List<TrainingEnrollRequestDTOOut> requestDTOOuts = new ArrayList<>();
        for(TrainingEnrollRequest request : trainingEnrollRequestRepository.findTrainingRequestByEmployeeIdAndStatus(id,status)){
            requestDTOOuts.add(convertToDTO(request));
        }
        return requestDTOOuts;
    }

    public List<TrainingEnrollRequestDTOOut> getRequestsByManagerIdAndStatus(Integer id, String status){
        List<TrainingEnrollRequestDTOOut> requestDTOOuts = new ArrayList<>();
        for(TrainingEnrollRequest request : trainingEnrollRequestRepository.findTrainingRequestByManagerIdAndStatus(id,status)){
            requestDTOOuts.add(convertToDTO(request));
        }
        return requestDTOOuts;
    }

    public List<TrainingEnrollRequestDTOOut> getRequestsByCompanyId(Integer id){
        List<TrainingEnrollRequestDTOOut> requestDTOOuts = new ArrayList<>();
        for(TrainingEnrollRequest request : trainingEnrollRequestRepository.findTrainingRequestByCompanyId(id)){
            requestDTOOuts.add(convertToDTO(request));
        }
        return requestDTOOuts;
    }

    public TrainingEnrollRequest convertToEntity(TrainingEnrollRequestDTOIn requestDTOIn){
        return new TrainingEnrollRequest(null, requestDTOIn.getStart_date(),requestDTOIn.getEnd_date(),null,
                requestDTOIn.getDescription(),null,null,null,null,null);
    }

    public TrainingEnrollRequestDTOOut convertToDTO(TrainingEnrollRequest request){

        return new TrainingEnrollRequestDTOOut(request.getId(),request.getTraining().getId(),request.getTraining().getName()
                ,request.getStart_date(),request.getEnd_date(),request.getTraining().getSkills().getId(),request.getStatus(),
                request.getDescription(),request.getNotes(),request.getManager().getId(),request.getEmployee().getId());
    }


}
