package org.example.skillflow.Repository;

import jakarta.transaction.Transactional;
import org.example.skillflow.Model.TrainingEnrollRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainingEnrollRequestRepository extends JpaRepository<TrainingEnrollRequest, Integer> {
    TrainingEnrollRequest findRequestById(Integer id);
    List<TrainingEnrollRequest> findTrainingRequestByEmployeeIdAndStatus(Integer id,String status);
    List<TrainingEnrollRequest> findTrainingRequestByEmployeeIdAndTrainingId(Integer id, Integer training_id);
    List<TrainingEnrollRequest> findTrainingRequestByManagerIdAndStatus(Integer id,String status);
    List<TrainingEnrollRequest> findTrainingRequestByCompanyId(Integer id);
    @Query("SELECT a FROM TrainingEnrollRequest a where a.employee.id=?1 and a.training.id=?2 ORDER BY a.start_date DESC LIMIT 1")
    TrainingEnrollRequest findNewestRequestByIdAndTrainingId(Integer employee_id, Integer trainingId);
    @Transactional
    void deleteByEmployeeId(Integer employee_id);
    @Transactional
    void deleteByManagerId(Integer manager_id);
    @Transactional
    void deleteByTrainingId(Integer training_Id);
}
