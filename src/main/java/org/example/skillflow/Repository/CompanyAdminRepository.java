package org.example.skillflow.Repository;


import org.example.skillflow.Model.CompanyAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyAdminRepository extends JpaRepository<CompanyAdmin,Integer> {
    CompanyAdmin findCompanyAdminById(Integer id);

    @Query("select lower(r.status), count(r) " + "from NewSkillRequest r " + "where r.companyAdmin.id = ?1 " + "group by lower(r.status)")
    List<Object[]> countByStatusForAdmin(Integer adminId);

}
