package org.example.skillflow.Repository;

import org.example.skillflow.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company ,Integer> {

    Company findCompanyById(Integer id);


}
