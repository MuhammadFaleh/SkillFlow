package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name must be not empty")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "username must be not empty")
    @Column(nullable = false)
    private String username;

    @NotEmpty(message = "CompanyName must be not empty")
    @Column(nullable = false , columnDefinition = "varchar(255)")
    private String email;

    @NotEmpty(message = "password must be not empty")
    @Column(nullable = false , columnDefinition = "varchar(255)")
    private String password;

    @NotEmpty(message = "record number must be not empty")
    @Column(nullable = false , columnDefinition = "varchar(10)")
    private String record_number;

    @NotEmpty(message = "country must be not empty")
    @Column(nullable = false , columnDefinition = "varchar(50)")
    private String county;

    @NotEmpty(message = "industry must be not empty")
    @Column(nullable = false)
    private String industry;


    private LocalDate created_at;

}
