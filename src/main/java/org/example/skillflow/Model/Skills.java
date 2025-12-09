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
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(200) not null")
    private String name;

    @Column(columnDefinition = "text not null")
    private String description;

    @ManyToOne
    @JsonIgnore
    private Company company;
    @ManyToMany
    @JoinTable(
            name = "employee_skills",
            joinColumns = @JoinColumn(name = "skills_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<Employee> employee;
//    @ManyToMany()
//    @JoinTable(
//            name = "",
//            joinColumns = @JoinColumn(name = ""),
//            inverseJoinColumns = @JoinColumn(name = "")
//    )
//    private Set<Project> project;
}
