package org.example.skillflow.Repository;

import org.example.skillflow.Model.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Company_RequestRepository extends JpaRepository<CompanyRequest, Integer> {

    CompanyRequest findCompany_RequestById(Integer id);

    List<CompanyRequest> getCompany_RequestByStatus(String status);

}
