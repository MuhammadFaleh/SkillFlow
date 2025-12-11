package org.example.skillflow.Repository;

import org.example.skillflow.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    Manager findManagerById(Integer id);
    List<Manager> findMangersByCompanyId(Integer id);
    Manager findManagerByEmployeeId(Integer id);
}
