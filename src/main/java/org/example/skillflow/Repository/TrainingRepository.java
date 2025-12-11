package org.example.skillflow.Repository;

import org.example.skillflow.Model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training , Integer> {
    Training findTrainingById(Integer id);
}
