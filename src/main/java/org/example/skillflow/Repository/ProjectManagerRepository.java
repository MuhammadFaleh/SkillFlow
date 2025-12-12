package org.example.skillflow.Repository;

import org.example.skillflow.Model.ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectManagerRepository extends JpaRepository<ProjectManager,Integer> {
    ProjectManager findProjectManagerById(Integer id);

    @Query("select pm from ProjectManager pm where pm.company.id = ?1")
    List<ProjectManager> findByCompanyId(Integer companyId);

    @Query("select pm from ProjectManager pm where pm.company.id = ?1 and pm.risk_load >= ?2")
    List<ProjectManager> findByCompanyIdAndRiskLoadGreaterThanEqual(Integer companyId, Integer limit);

}
