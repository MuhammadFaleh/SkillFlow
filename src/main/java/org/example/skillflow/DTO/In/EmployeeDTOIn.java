package org.example.skillflow.DTO.In;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTOIn {
    private Integer employee_id;
    @NotBlank(message = "username must not be empty")
    @Size(min = 5, max = 200, message = "username length must be between 5 and 200 characters long")
    private String username;
    @NotBlank(message = "full name must not be empty")
    @Size(min = 5, max = 200, message = "full name length must be between 5 and 200 characters long")
    private String full_name;
    @NotBlank(message = "gender must not be empty")
    @Pattern(regexp = "^[FM]$", message = "gender must be M or F (M for male, F for female)")
    private String gender;
    @NotNull(message = "age must not be empty")
    @Min(value = 20, message = "ages must be 20 or greater")
    private Integer age;
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
