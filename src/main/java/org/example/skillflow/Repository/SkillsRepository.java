package org.example.skillflow.Repository;

import org.example.skillflow.Model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillsRepository extends JpaRepository<Skills, Integer> {
    Skills findSkillsById(Integer id);
    List<Skills> findSkillsByCompanyId(Integer id);
    Skills findSkillsByNameAndCompanyId(String name, Integer id);
}
