package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(200) not null")
    private String name;

    @Column(columnDefinition = "text not null")
    private String description;


    //    -------- relational ----------
    @OneToMany(mappedBy = "training")
    private Set<TrainingSession> trainingSession;
    @ManyToOne
    @JsonIgnore
    private Skills skills;
    @ManyToOne
    @JsonIgnore
    private Company company;
}
