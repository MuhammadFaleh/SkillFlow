package org.example.skillflow.Repository;

import org.example.skillflow.Model.Admin;
import org.example.skillflow.Model.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Integer> {

    Admin findAdminById(Integer id);



}
