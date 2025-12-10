package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(40) not null unique")
    private String username;

    @Column(columnDefinition = "varchar(200) not null")
    private String full_name;

    @Column(columnDefinition = "varchar(1) not null check(gender='M' or gender='F')")
    private String gender;

    @Column(columnDefinition = "int not null check(age>20)")
    private Integer age;

    @Column(columnDefinition = "varchar(200) not null")
    private String email;

    @Column(columnDefinition = "varchar(255) not null")
    private String password;

    @ManyToOne
    @JsonIgnore
    private Company company;

    @OneToMany(mappedBy = "manager")
    private Set<Employee> employee;

    @OneToMany(mappedBy = "manager")
    private Set<AddSkillRequest> addSkillRequest;
}
