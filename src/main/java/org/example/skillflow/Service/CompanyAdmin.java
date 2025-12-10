package org.example.skillflow.Service;

import lombok.AllArgsConstructor;
import org.example.skillflow.Repository.CompanyAdminRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyAdmin {
    private final CompanyAdminRepository companyAdminRepository;
}
