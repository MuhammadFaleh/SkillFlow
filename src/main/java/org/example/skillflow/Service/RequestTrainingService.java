package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.RequestTrainingDTOIn;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Model.RequestTraining;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.RequestTrainingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestTrainingService {

    private final RequestTrainingRepository requestTrainingRepository;
    private final EmployeeRepository employeeRepository;

    public List<RequestTraining> getRequestTraining(){
        return requestTrainingRepository.findAll();
    }



    public void addRequestTraining(RequestTrainingDTOIn requestTrainingDTOIn){
        Employee employee = employeeRepository.findEmployeeById(requestTrainingDTOIn.getEmployee_id());
        RequestTraining lastRequestEmployee = requestTrainingRepository.findTheLatestRequest(requestTrainingDTOIn.getEmployee_id());
        if (lastRequestEmployee != null){
            if (lastRequestEmployee.getName().equalsIgnoreCase(requestTrainingDTOIn.getName())
                   && !lastRequestEmployee.getStatus().equalsIgnoreCase("rejected") ){
                throw new APIException("request it's already created");
            }
        }

        if (employee == null){
            throw new APIException("employee does't exist");
        }
        RequestTraining requestTraining = convertToEntity(requestTrainingDTOIn);
        requestTraining.setStart_date(LocalDateTime.now());
        requestTraining.setStatus("pending");
        requestTraining.setCreatedAt(LocalDateTime.now());
        requestTraining.setEmployee(employee);

        requestTrainingRepository.save(requestTraining);
    }


    public void updateRequestTraining(Integer requestTrainingId , RequestTrainingDTOIn requestTrainingDTOIn){
        RequestTraining requestTraining = requestTrainingRepository.findRequestTrainingById(requestTrainingId);
        if (requestTraining == null){
            throw new APIException("request training does't exist");
        }
        if (!"pending".equalsIgnoreCase(requestTraining.getStatus())){
            throw new APIException("unable to update request, it's already checked");
        }
        requestTraining = convertToEntity(requestTrainingDTOIn);
        requestTraining.setName(requestTrainingDTOIn.getName());
        requestTraining.setNotes(requestTrainingDTOIn.getNotes());
        requestTrainingRepository.save(requestTraining);
    }

    public void deleteRequestTraining(Integer requestTrainingId){
        RequestTraining requestTraining = requestTrainingRepository.findRequestTrainingById(requestTrainingId);
        if (requestTraining == null){
            throw new APIException("request training not found");
        }
        if (!"pending".equalsIgnoreCase(requestTraining.getStatus())){
            throw new APIException("unable to deleted request, it's already checked");
        }
        requestTrainingRepository.delete(requestTraining);
    }



    public RequestTraining convertToEntity(RequestTrainingDTOIn requestTrainingDTOIn){
        return new RequestTraining(requestTrainingDTOIn.getRequestTrainingId() , requestTrainingDTOIn.getStart_date() , requestTrainingDTOIn.getEnd_date()  , requestTrainingDTOIn.getName() , requestTrainingDTOIn.getNotes()
                , requestTrainingDTOIn.getRejectNote() , null , null , null , null , null);
    }
}
