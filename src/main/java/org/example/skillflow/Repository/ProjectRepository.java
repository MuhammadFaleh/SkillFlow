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

}
