package org.example.skillflow.Service;

import lombok.RequiredArgsConstructor;
import org.example.skillflow.API.APIException;
import org.example.skillflow.DTO.In.EmployeeDTOIn;
import org.example.skillflow.DTO.In.SkillsDTOIn;
import org.example.skillflow.DTO.Out.EmployeeDTOOut;
import org.example.skillflow.DTO.Out.SkillsDTOOut;
import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.Employee;
import org.example.skillflow.Model.Skills;
import org.example.skillflow.Repository.CompanyRepository;
import org.example.skillflow.Repository.SkillsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SkillsService {
    private final SkillsRepository skillsRepository;
    private final CompanyRepository companyRepository;

    public List<SkillsDTOOut> getSkills(){
        List<SkillsDTOOut> skillsDTOOuts = new ArrayList<>();
        for (Skills skills : skillsRepository.findAll()){
            skillsDTOOuts.add(convertToDTO(skills));
        }
        return skillsDTOOuts;
    }

    public void createSkills(SkillsDTOIn skillsDTOIn){
        Company company = companyRepository.findCompanyById(skillsDTOIn.getCompany_id());
        if(company == null){
            throw new APIException("company doesn't exist");
        }
        Skills skills = convertToEntity(skillsDTOIn);
        skills.setCompany(company);
        skillsRepository.save(skills);
    }

    public void updateSkills(Integer id, SkillsDTOIn skillsDTOIn){
        Company company = companyRepository.findCompanyById(skillsDTOIn.getCompany_id());
        Skills skills = skillsRepository.findSkillsById(id);
        if(company == null){
            throw new APIException("company doesn't exist");
        }

        if(skills == null){
            throw new APIException("skill doesn't exist");
        }
        skills.setDescription(skillsDTOIn.getDescription());
        skills.setName(skillsDTOIn.getName());
        skillsRepository.save(skills);
    }

    public void deleteSkills(Integer id, Integer company_id){
        Company company = companyRepository.findCompanyById(company_id);
        Skills skills = skillsRepository.findSkillsById(id);

        if(company == null){
            throw new APIException("company doesn't exist");
        }

        if(skills == null || !skills.getCompany().getId().equals(company_id)){
            throw new APIException("skill doesn't exist");
        }

        skillsRepository.delete(skills);
    }

    public Skills convertToEntity(SkillsDTOIn skillsDTOIn){
        return new Skills(skillsDTOIn.getSkills_id(), skillsDTOIn.getName(),skillsDTOIn.getDescription(),
                null,null, null,null);
    }

    public SkillsDTOOut convertToDTO(Skills skills){
        return new SkillsDTOOut(skills.getId(),skills.getName(),skills.getDescription(),skills.getCompany().getId());
    }
}
