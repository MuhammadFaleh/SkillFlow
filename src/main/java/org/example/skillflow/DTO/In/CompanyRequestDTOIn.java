package org.example.skillflow.DTO.In;

<<<<<<< HEAD
public class CompanyRequestDTOIn {
=======
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CompanyRequestDTOIn {


    private Integer companyRequestId;

    @NotEmpty(message = "fullName must be not empty")
    @Size(min = 5, max = 200, message = "username length must be between 5 and 200 characters long")
    private String fullName;

    @Email(message = "email must be has @")
    @NotEmpty(message = "email must be not empty")
    private String email;

    @NotEmpty(message = "CompanyName must be not empty")
    @Size(min = 5, max = 200, message = "company name length must be between 5 and 200 characters long")
    private String companyName;

    @NotEmpty(message = "record number must be not empty")
    @Size(min = 10, max = 11, message = "record number length must be 10 characters long")
    private String recordNumber;

    @NotEmpty(message = "country must be not empty")
    @Size(min = 2, max = 50, message = "country length must be between 2 and 50 characters long")
    private String country;

    @NotEmpty(message = "industry must be not empty")
    private String industry;


>>>>>>> test
}
