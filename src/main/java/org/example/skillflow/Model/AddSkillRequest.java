package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity // change diagrams
public class AddSkillRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "timestamp not null")
    private LocalDateTime start_date;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime end_date;

    @Column(columnDefinition = "varchar(10) not null")
    private String status;

    @Column(columnDefinition = "text not null ")
    private String description;

    @Column(columnDefinition = "text")
    private String notes;

    @ManyToOne
    @JsonIgnore
    private Skills skills;

    @ManyToOne
    @JsonIgnore
    private Manager manager;

    @ManyToOne
    @JsonIgnore
    private Employee employee;

    @ManyToOne
    @JsonIgnore
    private Company company;
}
