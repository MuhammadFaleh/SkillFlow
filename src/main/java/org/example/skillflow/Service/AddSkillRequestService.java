package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.AddSkillRequestDTOIn;
import org.example.skillflow.DTO.Out.AddSkillRequestDTOOut;
import org.example.skillflow.Model.AddSkillRequest;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Model.Skills;
import org.example.skillflow.Repository.AddSkillRequestRepository;
import org.example.skillflow.Repository.EmployeeRepository;
import org.example.skillflow.Repository.SkillsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddSkillRequestService {
    private final AddSkillRequestRepository addSkillRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillsRepository skillsRepository;

    public List<AddSkillRequestDTOOut> getRequests(){
        List<AddSkillRequestDTOOut> addSkillRequestDTOOuts = new ArrayList<>();
        for(AddSkillRequest addSkillRequest : addSkillRequestRepository.findAll()){
            addSkillRequestDTOOuts.add(convertToDTO(addSkillRequest));
        }
        return addSkillRequestDTOOuts;
    }

    public void createRequest(AddSkillRequestDTOIn addSkillRequestDTOIn){
        Employee employee = employeeRepository.findEmployeeById(addSkillRequestDTOIn.getEmployee_id());
        Skills skills = skillsRepository.findSkillsById(addSkillRequestDTOIn.getSkill_id());
        AddSkillRequest addSkillRequest =
                addSkillRequestRepository.findNewestRequestById(addSkillRequestDTOIn.getEmployee_id(), addSkillRequestDTOIn.getSkill_id());

        if(addSkillRequest != null){
            if(addSkillRequest.getStatus().equalsIgnoreCase("pending") &&
                    addSkillRequest.getSkills().getId().equals(addSkillRequestDTOIn.getSkill_id())) {
                throw new APIException("request not checked yet you can update it");
            }
            if(addSkillRequest.getStatus().equalsIgnoreCase("approved") &&
                    addSkillRequest.getSkills().getId().equals(addSkillRequestDTOIn.getSkill_id())){
                throw new APIException("request is already approved");
            }
        }

        if(employee == null){
            throw new APIException("no employee found");
        }

        if(skills == null || !employee.getCompany().getId().equals(skills.getCompany().getId())){
            throw new APIException("no skill found in the company record consider creating a request to add it");
        }

        if(employee.getManager() == null){
            throw new APIException("employee doesn't have a manager");
        }

        AddSkillRequest newRequest = convertToEntity(addSkillRequestDTOIn);
        newRequest.setStart_date(LocalDateTime.now());
        newRequest.setStatus("pending");
        newRequest.setSkills(skills);
        newRequest.setEmployee(employee);
        newRequest.setManager(employee.getManager());
        newRequest.setCompany(employee.getCompany());
        addSkillRequestRepository.save(newRequest);
    }

    public void updateRequest(Integer id, AddSkillRequestDTOIn addSkillRequestDTOIn){
        AddSkillRequest addSkillRequest =
                addSkillRequestRepository.findAddSkillRequestById(id);

       if(addSkillRequest == null){
           throw new APIException("no request found please make one");
       }

       if(!addSkillRequest.getEmployee().getId().equals(addSkillRequestDTOIn.getEmployee_id())){
           throw new APIException("unauthorized to update the request");
       }

       if(!addSkillRequest.getStatus().equalsIgnoreCase("pending")){
           throw new APIException("unable to update request, it's already checked");
       }

        addSkillRequest.setDescription(addSkillRequestDTOIn.getDescription());
        addSkillRequestRepository.save(addSkillRequest);
    }

    public void deleteRequest(Integer id , Integer employee_id){
        AddSkillRequest addSkillRequest =
                addSkillRequestRepository.findAddSkillRequestById(id);
        Employee employee = employeeRepository.findEmployeeById(employee_id);

        if(addSkillRequest == null || employee==null){
            throw new APIException("no request found please make one");
        }

        if(!employee_id.equals(addSkillRequest.getEmployee().getId())){
            throw new APIException("unauthorized to delete the request");
        }

        if(addSkillRequest.getStatus().equalsIgnoreCase("approved") ||
                addSkillRequest.getStatus().equalsIgnoreCase("rejected")){
            throw new APIException("unable to delete a request, it's already checked");
        }

        addSkillRequestRepository.delete(addSkillRequest);
    }

    public void approveSkill(Integer id, Integer manager_id){
        AddSkillRequest addSkillRequest = addSkillRequestRepository.findAddSkillRequestById(id);

        if(addSkillRequest == null){
            throw new APIException("no request found please make one");
        }

        if(addSkillRequest.getManager() == null ||
                !manager_id.equals(addSkillRequest.getManager().getId())){
            throw new APIException("manager id doesn't match the employee manager id");
        }

        if(!addSkillRequest.getStatus().equalsIgnoreCase("pending")){
            throw new APIException("request is already checked");
        }
        Skills skills = skillsRepository.findSkillsById(addSkillRequest.getSkills().getId());
        // send email or whatsapp message
        Employee employee = addSkillRequest.getEmployee();
        employee.getSkills().add(addSkillRequest.getSkills());
        employeeRepository.save(employee);
        addSkillRequest.setStatus("approved");
        addSkillRequest.setEnd_date(LocalDateTime.now());
        addSkillRequestRepository.save(addSkillRequest);
        skills.getEmployee().add(employee);
        skillsRepository.save(skills);
    }

    public void rejectSkill(Integer id, Integer manager_id, AddSkillRequestDTOIn addSkillRequestDTOIn){
        AddSkillRequest addSkillRequest = addSkillRequestRepository.findAddSkillRequestById(id);


        if(addSkillRequest == null){
            throw new APIException("no request found please make one");
        }

        if(addSkillRequest.getManager() == null ||
                !manager_id.equals(addSkillRequest.getManager().getId())){
            throw new APIException("manager id doesn't match the employee manager id");
        }

        if(!addSkillRequest.getStatus().equalsIgnoreCase("pending")){
            throw new APIException("request is already checked");
        }
        // send email or whatsapp message
        addSkillRequest.setStatus("rejected");
        addSkillRequest.setEnd_date(LocalDateTime.now());
        addSkillRequest.setNotes(addSkillRequestDTOIn.getNotes());
        addSkillRequestRepository.save(addSkillRequest);
    }

    public List<AddSkillRequestDTOOut> getRequestsByEmployeeId(Integer id){
        List<AddSkillRequestDTOOut> addSkillRequestDTOOuts = new ArrayList<>();
        for(AddSkillRequest addSkillRequest : addSkillRequestRepository.findAddSkillRequestByEmployeeId(id)){
            addSkillRequestDTOOuts.add(convertToDTO(addSkillRequest));
        }
        return addSkillRequestDTOOuts;
    }

    public List<AddSkillRequestDTOOut> getRequestsByManagerIdAndStatus(Integer id, String status){
        List<AddSkillRequestDTOOut> addSkillRequestDTOOuts = new ArrayList<>();
        for(AddSkillRequest addSkillRequest : addSkillRequestRepository.findAddSkillRequestByManagerIdAndStatus(id, status)){
            addSkillRequestDTOOuts.add(convertToDTO(addSkillRequest));
        }
        return addSkillRequestDTOOuts;
    }

    public List<AddSkillRequestDTOOut> getRequestsByCompanyId(Integer id){
        List<AddSkillRequestDTOOut> addSkillRequestDTOOuts = new ArrayList<>();
        for(AddSkillRequest addSkillRequest : addSkillRequestRepository.findAddSkillRequestByCompanyId(id)){
            addSkillRequestDTOOuts.add(convertToDTO(addSkillRequest));
        }
        return addSkillRequestDTOOuts;
    }

    public AddSkillRequest convertToEntity(AddSkillRequestDTOIn addSkillRequestDTOIn){
        return new AddSkillRequest(null,addSkillRequestDTOIn.getStart_date(),
                addSkillRequestDTOIn.getEnd_date(),null, addSkillRequestDTOIn.getDescription()
                ,addSkillRequestDTOIn.getNotes(), null, null,null,null);
    }

    public AddSkillRequestDTOOut convertToDTO(AddSkillRequest addSkillRequest){
        return new AddSkillRequestDTOOut(addSkillRequest.getId(), addSkillRequest.getStart_date(),addSkillRequest.getEnd_date(),
                addSkillRequest.getEmployee().getId(), addSkillRequest.getSkills().getId(),addSkillRequest.getSkills().getName(),
                addSkillRequest.getStatus(),addSkillRequest.getDescription(),addSkillRequest.getNotes(),
                addSkillRequest.getManager().getId());
    }
}
