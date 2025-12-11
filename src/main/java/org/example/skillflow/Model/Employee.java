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
public class Employee {
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

    @Column(columnDefinition = "varchar(200)", unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "varchar(254) not null")
    private String password;
    @ManyToOne
    @JsonIgnore
    private Project project;
    @OneToMany(mappedBy = "employee")
    private Set<AddSkillRequest> addSkillRequests;
    @ManyToOne
    @JsonIgnore
    private Manager manager;
    @ManyToOne
    @JsonIgnore
    private Company company;
    @ManyToMany(mappedBy = "employee")
    private Set<Skills> skills;
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private Set<NewSkillRequest> newSkillRequests;

}
