package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column(columnDefinition = "varchar(10) not null check(status = 'pending' or status='approved' or status='rejected')")
    private String status;

    @Column(columnDefinition = "text not null check(length(description) <=300)")
    private String description;

    @Column(columnDefinition = "text check(length(notes) <=300)")
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
