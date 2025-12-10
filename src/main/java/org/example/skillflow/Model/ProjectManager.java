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

    @Column(columnDefinition = "int not null check(risk_load ='low' or risk_load ='medium' or risk_load ='high' or risk_load ='critical' )")
    private String risk_load;

    @ManyToOne
    @JsonIgnore
    private Company company;

    @ManyToMany(mappedBy = "projectManagers")
    @JoinTable(
            name = "projectManager_projects",
            joinColumns = @JoinColumn(name = "projectManager_Id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;


}
