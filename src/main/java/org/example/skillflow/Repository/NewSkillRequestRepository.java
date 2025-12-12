package org.example.skillflow.Repository;


import org.example.skillflow.Model.NewSkillRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewSkillRequestRepository extends JpaRepository<NewSkillRequest,Integer> {
    NewSkillRequest findNewSkillRequestById(Integer id);

    @Query("select r from NewSkillRequest r where r.employee.id = ?1")
    List<NewSkillRequest> findByEmployeeId(Integer employeeId);

    @Query("select r from NewSkillRequest r where r.employee.id = ?1 and lower(r.status) = lower(?2)")
    List<NewSkillRequest> findByEmployeeIdAndStatus(Integer employeeId, String status);

    @Query("select r from NewSkillRequest r where r.companyAdmin.id = ?1")
    List<NewSkillRequest> findByCompanyAdminId(Integer adminId);

    @Query("select r from NewSkillRequest r where r.companyAdmin.id = ?1 and lower(r.status) = 'pending'")
    List<NewSkillRequest> findPendingByCompanyAdminId(Integer adminId);

}