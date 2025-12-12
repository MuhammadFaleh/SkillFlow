package org.example.skillflow.Repository;

import jakarta.transaction.Transactional;
import org.example.skillflow.Model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {
    TrainingSession findSessionById(Integer id);
    List<TrainingSession> findTrainingSessionByEmployeeIdAndStatus(Integer id,String status);
    @Query("SELECT a FROM TrainingSession a where a.employee.id=?1 and a.training.id=?2 ORDER BY a.start_date DESC LIMIT 1")
    TrainingSession findSessionByNewestTrainingIdAndEmployee(Integer id, Integer training_id);
    @Transactional
    void deleteByEmployeeId(Integer employeeId);
    @Transactional
    void deleteByTrainingId(Integer skills);
}
