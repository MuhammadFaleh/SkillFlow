package org.example.skillflow.Repository;

import org.example.skillflow.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findEmployeeById(Integer id);
    List<Employee> findEmployeeByCompanyId(Integer id);
    List<Employee> findEmployeeByManagerId(Integer id);

    List<Employee> findEmployeeByCompanyIdAndSkillsId(Integer id, Integer Skill);

}
