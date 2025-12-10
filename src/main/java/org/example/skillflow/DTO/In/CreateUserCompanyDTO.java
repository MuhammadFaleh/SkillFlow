package org.example.skillflow.DTO.In;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserCompanyDTO {

    private Integer companyId;

    @NotBlank(message = "username must not be empty")
    @Size(min = 5, max = 200, message = "username length must be between 5 and 200 characters long")
    private String username;

    @NotBlank(message = "full name must not be empty")
    @Size(max = 200, message = "email length must be less than 200 characters long")
    @Email(message = "email must be in valid format")
    private String email;

    @NotBlank(message = "password must not be empty")
    @Size(min = 8, max = 40, message = "password length must be between 8 and 40 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$&+,:;=?@#|'<>.^*()%!-]).*$",
            message = "please enter at least one number and special character")
    private String password;
}
