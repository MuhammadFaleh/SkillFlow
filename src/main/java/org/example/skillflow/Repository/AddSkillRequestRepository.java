package org.example.skillflow.Repository;

import org.example.skillflow.Model.AddSkillRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddSkillRequestRepository extends JpaRepository<AddSkillRequest, Integer> {
    AddSkillRequest findAddSkillRequestById(Integer id);
    @Query("SELECT a FROM AddSkillRequest a where a.employee_id=?1 and a.skills_id=?2 ORDER BY a.start_date DESC LIMIT 1")
    AddSkillRequest findNewestRequestById(Integer id, Integer skill_id);
    List<AddSkillRequest> findAddSkillRequestByCompany_id(Integer id);
    List<AddSkillRequest> findAddSkillRequestByEmployee_id(Integer id);
}
