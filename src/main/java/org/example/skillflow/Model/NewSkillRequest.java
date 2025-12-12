package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;



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

    @Column(columnDefinition = "varchar(20)")
    private String status;

    @Column(columnDefinition = "timestamp not null")
    private LocalDateTime start_date;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime end_date;

    @ManyToOne
    @JsonIgnore
    private CompanyAdmin companyAdmin;

    @ManyToOne
    @JsonIgnore
    private Employee employee;

    @ManyToOne
    @JsonIgnore
    private Company company;
}