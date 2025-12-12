package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(30) unique not null")
    private String username;

    @Column(columnDefinition = "varchar(30) not null")
    private String password;

    @Column(columnDefinition = "varchar(200) not null")
    private String full_name;

    @Column(columnDefinition = "varchar(1) not null check(gender='M' or gender='F')")
    private String gender;

    @Column(columnDefinition = "int not null check(age>20)")
    private Integer age;

    @Column(columnDefinition = "varchar(200) unique not null")
    private String email;

    @Column(columnDefinition = "int")
    private Integer risk_load;

    @ManyToOne
    @JsonIgnore
    private Company company;

    @ManyToMany(mappedBy = "projectManagers")
    private Set<Project> projects;


}
