package org.example.skillflow.Repository;

import org.example.skillflow.Model.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRequestRepository extends JpaRepository<CompanyRequest , Integer> {

    CompanyRequest findCompanyRequestById(Integer id);

    List<CompanyRequest> getCompanyRequestByStatus(String status);

}
