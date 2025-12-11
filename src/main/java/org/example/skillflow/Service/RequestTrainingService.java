package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.RequestTrainingDTOIn;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Model.RequestTraining;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.RequestTrainingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        if (employee == null){
            throw new APIException("employee does't exist");
        }
        RequestTraining requestTraining = convertToEntity(requestTrainingDTOIn);
        requestTraining.setStatus("pending");
        requestTraining.setCreatedAt(LocalDate.now());
        requestTraining.setEmployee(employee);

        requestTrainingRepository.save(requestTraining);
    }

    public void updateRequestTraining(Integer requestTrainingId , RequestTrainingDTOIn requestTrainingDTOIn){

    }
    public RequestTraining convertToEntity(RequestTrainingDTOIn requestTrainingDTOIn){
        return new RequestTraining(requestTrainingDTOIn.getRequestTrainingId() , requestTrainingDTOIn.getName() , requestTrainingDTOIn.getNotes() , null , LocalDate.now() , null , null);
    }
}
