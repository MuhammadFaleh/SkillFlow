package org.example.skillflow.Repository;

import org.example.skillflow.Model.Admin;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
=======
import org.example.skillflow.Model.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
>>>>>>> test
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Integer> {

    Admin findAdminById(Integer id);


<<<<<<< HEAD
=======

>>>>>>> test
}
