package org.example.skillflow.DTO.Out;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTOOut {

    private Integer id;

    private String name;

    private String username;

    private String email;

    private String record_number;

    private String country;

    private String industry;
}
