package org.example.skillflow.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyAdmin {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(columnDefinition = "varchar(40) not null unique")
        private String username;

        @Column(columnDefinition = "varchar(40) not null unique")
         private String email;

        @Column(columnDefinition = "varchar(255) not null")
        private String password;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "company_id")
        private Company company;

         @OneToMany(mappedBy = "companyAdmin")
         private Set<RequestTraining> requestTraining;
         @OneToMany(mappedBy = "companyAdmin")
         private Set<NewSkillRequest> newSkillRequests;

}

