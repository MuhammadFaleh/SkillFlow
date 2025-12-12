package org.example.skillflow.Repository;

import org.example.skillflow.Model.RequestTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTrainingRepository extends JpaRepository<RequestTraining , Integer> {

    @Query("SELECT f FROM RequestTraining f where f.employee.id=?1 ORDER BY f.createdAt DESC LIMIT 1")
    RequestTraining findTheLatestRequest(Integer employeeId);

    RequestTraining findRequestTrainingById(Integer id);

}
