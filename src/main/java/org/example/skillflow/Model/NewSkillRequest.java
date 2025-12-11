package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewSkillRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(200) not null")
    private String name;

    @Column(columnDefinition = "text not null")
    private String description;

    @Column(columnDefinition = "varchar(20) not null check('pending','approved','rejected')")
    private String status;

    @ManyToOne
    @JsonIgnore
    @JoinTable(
            name = "company_skillsRequests",
            joinColumns = @JoinColumn(name = "newSkillRequest_id"),
            inverseJoinColumns = @JoinColumn(name = "companyAdmin_id ")
    )
    private CompanyAdmin companyAdmin;

    @ManyToOne
    @JsonIgnore
    @JoinTable(
            name = "employee_skillsRequests",
            joinColumns = @JoinColumn(name = "newSkillRequest_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Employee employee;
}
