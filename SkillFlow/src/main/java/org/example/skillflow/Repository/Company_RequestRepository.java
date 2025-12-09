package org.example.skillflow.Repository;

import org.example.skillflow.Model.Company;
import org.example.skillflow.Model.Company_Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Company_RequestRepository extends JpaRepository<Company_Request , Integer> {

    Company_Request findCompany_RequestById(Integer id);

    List<Company_Request> getCompany_RequestByStatus(String status);

}
