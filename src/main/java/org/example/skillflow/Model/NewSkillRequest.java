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

    @NotEmpty(message = "skill name must not be empty")
    @Column(columnDefinition = "varchar(200) not null")
    private String name;

    @NotEmpty(message = "skill description must not be empty")
    @Column(columnDefinition = "text not null")
    private String description;

    @ManyToOne
    @JsonIgnore
    private CompanyAdmin companyAdmin;

    @ManyToOne
    @JsonIgnore
    private Employee employee;
}
