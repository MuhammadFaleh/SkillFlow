package org.example.skillflow.Repository;

import org.example.skillflow.Model.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRequestRepository extends JpaRepository<CompanyRequest, Integer> {

    CompanyRequest findCompanyRequestById(Integer id);

    List<CompanyRequest> getCompanyRequestByStatus(String status);

    @Query("SELECT f FROM CompanyRequest f where f.recordNumber=?1 ORDER BY f.requestDate DESC LIMIT 1")
    CompanyRequest findTheLatestRequest(String recordeNumber);

}