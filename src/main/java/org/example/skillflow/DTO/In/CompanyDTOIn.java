package org.example.skillflow.DTO.In;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.skillflow.vaildationGroups.ValidationGroup1;

@Data
@AllArgsConstructor
public class CompanyDTOIn {

    private Integer companyId;

    @NotBlank(message = "Name must be not empty")
    @Size(min = 5, max = 200, message = "name length must be between 5 and 200 characters long")
    private String name;

    @NotBlank(message = "username must not be empty")
    @Size(min = 5, max = 200, message = "username length must be between 5 and 200 characters long")
    private String username;

    @NotBlank(message = "email must not be empty")
    @Size(max = 200, message = "email length must be less than 200 characters long")
    @Email(message = "email must be in valid format")
    private String email;

    @NotBlank(message = "password must be not empty")
    @Size(min = 8, max = 40, message = "password length must be between 8 and 40 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$&+,:;=?@#|'<>.^*()%!-]).*$",
            message = "please enter at least one number and special character")
    private String password;

    @NotBlank(message = "record number must be not empty")
    private String record_number;

    @NotBlank(message = "country must be not empty",  groups = ValidationGroup1.class)
    @Size(min = 4, max = 100, message = "county length must be between 4 and 100 characters long",  groups = ValidationGroup1.class)
    private String country;

    @NotBlank(message = "industry must be not empty", groups = ValidationGroup1.class)
    @Size(min = 4, max = 100, message = "industry length must be between 4 and 100 characters long", groups = ValidationGroup1.class)
    private String industry;

}
