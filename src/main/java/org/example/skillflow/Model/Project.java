package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(255) not null")
    private String description;

    @Column(columnDefinition = "varchar(30) not null check(status='pending' or status='approved' or status='in_progress'or status='rejected')")
    private String status;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime start_date;

    @Column(columnDefinition = "datetime")
    private LocalDateTime end_date;

    @Column(columnDefinition = "varchar(30) not null check(risk ='low' or risk='medium' or risk='high' or risk='critical')")
    private String risk;

    @ManyToOne
    @JsonIgnore
    private Company company;

    @ManyToMany
    @JsonIgnore
    private Set<ProjectManager> projectManagers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<Employee> employees;

    @ManyToMany
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "projects_id"),
            inverseJoinColumns = @JoinColumn(name = "skills_id")
    )
    private Set<Skills> skills;
}