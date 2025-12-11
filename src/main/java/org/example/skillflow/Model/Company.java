package org.example.skillflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , length = 200)
    private String name;

    @Column(nullable = false , length = 200 , unique = true)
    private String username;

    @Column(nullable = false , unique = true , length = 200)
    private String email;

    @Column(nullable = false , length = 254 )
    private String password;

    @Column(nullable = false , length = 10 , unique = true)
    private String record_number;

    @Column(name = "country", nullable = false , length = 200)
    private String country;

    @Column( nullable = false , length = 200)
    private String industry;

    private LocalDate created_at;

//    -------- relational ----------
    @OneToMany(mappedBy = "company")
    private Set<CompanyAdmin> companyAdmin;
    @OneToMany(mappedBy = "company")
    private Set<Employee> employee;
    @OneToMany(mappedBy = "company")
    private Set<Manager> manager;
    @OneToMany(mappedBy = "company")
    private Set<Skills> skills;
    @OneToMany(mappedBy = "company")
    private Set<Training> trainings;
}
