package org.example.skillflow.Repository;

import org.example.skillflow.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    Manager findManagerById(Integer id);
}
