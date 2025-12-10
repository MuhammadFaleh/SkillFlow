package org.example.skillflow.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAdminDTOOut {

    private Integer companyAdmin_id;
    private String username;
    private Integer company_id;

}
