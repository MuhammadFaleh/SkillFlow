package org.example.skillflow.Repository;

import org.example.skillflow.Model.CompanyRequest;
import org.example.skillflow.Model.RequestTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTrainingRepository extends JpaRepository<RequestTraining , Integer> {
    @Query("SELECT f FROM CompanyRequest f where f.recordNumber=?1 ORDER BY f.requestDate DESC LIMIT 1")
    CompanyRequest findTheLatestRequest(String recordeNumber);

    RequestTraining findRequestTrainingById(Integer id);

}
