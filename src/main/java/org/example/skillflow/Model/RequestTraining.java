package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "timestamp not null")
    private LocalDateTime start_date;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime end_date;

    @Column(columnDefinition = "varchar(200) not null")
    private String name;

    @Column(nullable = false)
    private String notes;

    @Column(columnDefinition = "text")
    private String rejectNote;

    @Column(columnDefinition = "varchar(20) default 'PENDING'")
    private String status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    private CompanyAdmin companyAdmin;

    @ManyToOne
    @JsonIgnore
    private Employee employee;

}
