package org.example.skillflow.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "username must be not empty")
    @Column(columnDefinition = "varchar(40) not null")
    private String username;

    @NotEmpty(message = "email must be not empty")
    @Column(columnDefinition = "varchar(60) not null")
    private String email;

    @NotEmpty(message = "password must be not empty")
    @Column(columnDefinition = "varchar(100) not null")
    private String password;

}
