package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.NewSkillRequestDTOIn;
import org.example.skillflow.DTO.Out.NewSkillRequestDTOOut;
import org.example.skillflow.Model.CompanyAdmin;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Model.NewSkillRequest;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.NewSkillRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewSkillRequestService {

    private final NewSkillRequestRepository newSkillRequestRepository;
    private final EmployeeRepository employeeRepository;

    public List<NewSkillRequestDTOOut> getNewSkillRequests() {
        List<NewSkillRequestDTOOut> newSkillRequestDTOOuts = new ArrayList<>();

        for (NewSkillRequest request : newSkillRequestRepository.findAll()) {
            newSkillRequestDTOOuts.add(convertToDTO(request));
        }

        return newSkillRequestDTOOuts;
    }

    public void createNewSkillRequest(NewSkillRequestDTOIn newSkillRequestDTOIn) {

        Employee employee = employeeRepository.findEmployeeById(newSkillRequestDTOIn.getEmployee_id());
        if (employee == null) {
            throw new APIException("employee not found with id: " + newSkillRequestDTOIn.getEmployee_id());
        }

        NewSkillRequest newSkillRequest = convertToEntity(newSkillRequestDTOIn);
        newSkillRequest.setEmployee(employee);
        newSkillRequest.setCompanyAdmin(null);
        newSkillRequest.setStatus("pending");
        newSkillRequest.setStart_date(LocalDateTime.now());
        newSkillRequest.setEnd_date(null);
        newSkillRequestRepository.save(newSkillRequest);

    }

    public void updateNewSkillRequest(Integer requestId, NewSkillRequestDTOIn newSkillRequestDTOIn) {

        NewSkillRequest newSkillRequest = newSkillRequestRepository.findNewSkillRequestById(requestId);
        if (newSkillRequest == null) {
            throw new APIException("new skill request not found with id: " + requestId);
        }

        newSkillRequest.setName(newSkillRequestDTOIn.getName());
        newSkillRequest.setDescription(newSkillRequestDTOIn.getDescription());

        newSkillRequestRepository.save(newSkillRequest);
    }

    public void deleteNewSkillRequest(Integer requestId) {

        NewSkillRequest newSkillRequest = newSkillRequestRepository.findNewSkillRequestById(requestId);
        if (newSkillRequest == null) {
            throw new APIException("new skill request not found with id: " + requestId);
        }

        if (!newSkillRequest.getStatus().equalsIgnoreCase("pending")) {
            throw new APIException("only pending requests can be deleted: current status: " + newSkillRequest.getStatus());
        }

        Employee employee = newSkillRequest.getEmployee();
        if (employee != null && employee.getNewSkillRequests() != null) {

            employee.getNewSkillRequests().remove(newSkillRequest);
        }

        CompanyAdmin companyAdmin = newSkillRequest.getCompanyAdmin();
        if (companyAdmin != null && companyAdmin.getNewSkillRequests() != null) {
            companyAdmin.getNewSkillRequests().remove(newSkillRequest);
        }

        newSkillRequestRepository.delete(newSkillRequest);
    }

    public NewSkillRequest convertToEntity(NewSkillRequestDTOIn newSkillRequestDTOIn) {
        return new NewSkillRequest(newSkillRequestDTOIn.getRequest_id(), newSkillRequestDTOIn.getName(), newSkillRequestDTOIn.getDescription(), null, null, null,null,null);
    }

    public NewSkillRequestDTOOut convertToDTO(NewSkillRequest newSkillRequest) {
        return new NewSkillRequestDTOOut(newSkillRequest.getId(), newSkillRequest.getName(), newSkillRequest.getDescription(), newSkillRequest.getStatus(),null,null);
    }
}