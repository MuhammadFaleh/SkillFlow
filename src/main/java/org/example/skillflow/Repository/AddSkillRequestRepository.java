package org.example.skillflow.Repository;

import jakarta.transaction.Transactional;
import org.example.skillflow.Model.AddSkillRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddSkillRequestRepository extends JpaRepository<AddSkillRequest, Integer> {
    AddSkillRequest findAddSkillRequestById(Integer id);
    @Query("SELECT a FROM AddSkillRequest a where a.employee.id=?1 and a.skills.id=?2 ORDER BY a.start_date DESC LIMIT 1")
    AddSkillRequest findNewestRequestById(Integer id, Integer skill_id);
    List<AddSkillRequest> findAddSkillRequestByCompanyId(Integer id);
    List<AddSkillRequest> findAddSkillRequestByEmployeeId(Integer id);
    List<AddSkillRequest> findAddSkillRequestByManagerId(Integer id);
    List<AddSkillRequest> findAddSkillRequestByManagerIdAndStatus(Integer id, String status);

    @Transactional
    void deleteByEmployeeId(Integer employeeId);
    @Transactional
    void deleteByManagerId(Integer managerId);
    @Transactional
    void deleteBySkillsId(Integer skills);
}
