package org.example.skillflow.Repository;

import jakarta.transaction.Transactional;
import org.example.skillflow.Model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
    Training findTrainingById(Integer id);
    List<Training> findTrainingBySkillsId(Integer id);
    Training findTrainingByCompanyIdAndName(Integer id,String name);
    List<Training> findTrainingByCompanyId(Integer id);
    @Transactional
    void deleteBySkillsId(Integer skills);
}
