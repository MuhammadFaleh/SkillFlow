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

    @Column(nullable = false , unique = true , length = 20)
    private String username;

    @Column(nullable = false , unique = true , length = 200)
    private String email;

    @Column(nullable = false , length = 50)
    private String password;

}
