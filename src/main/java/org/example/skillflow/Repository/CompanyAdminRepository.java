package org.example.skillflow.Repository;

import org.example.skillflow.Model.CompanyAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyAdminRepository extends JpaRepository<CompanyAdmin , Integer> {
    CompanyAdmin findCompanyAdminById(Integer id);
}
