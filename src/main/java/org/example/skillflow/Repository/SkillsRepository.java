package org.example.skillflow.Repository;

import org.example.skillflow.Model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepository extends JpaRepository<Skills, Integer> {
    Skills findSkillsById(Integer id);
}
