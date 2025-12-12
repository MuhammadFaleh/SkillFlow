package org.example.skillflow.Repository;

import org.example.skillflow.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
    Project findProjectById(Integer id);
    List<Project> findProjectByCompanyId(Integer id);

    List<Project> findProjectByCompanyIdAndSkillsId(Integer id, Integer skill);

    @Query("select p from Project p where p.company.id = ?1")
    List<Project> findByCompanyId(Integer companyId);

    @Query("select p from Project p where p.company.id = ?1 and lower(p.status) = lower(?2)")
    List<Project> findByCompanyIdAndStatus(Integer companyId, String status);

    @Query("select p from Project p where p.company.id = ?1 and lower(p.risk) = lower(?2)")
    List<Project> findByCompanyIdAndRisk(Integer companyId, String riskLevel);
}
