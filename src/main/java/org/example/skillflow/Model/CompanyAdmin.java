package org.example.skillflow.Model;

<<<<<<< HEAD

=======
>>>>>>> test
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


<<<<<<< HEAD
import java.util.Set;

=======
>>>>>>> test
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyAdmin {

<<<<<<< HEAD
=======

>>>>>>> test
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotEmpty(message = "username must not be empty")
        @Column(columnDefinition = "varchar(40) not null unique")
        private String username;

        @NotEmpty(message = "password must not be empty")
        @Column(columnDefinition = "varchar(255) not null")
        private String password;

        @ManyToOne
        @JsonIgnore
        private Company company;
<<<<<<< HEAD
}
=======


//         @OneToMany(mappedBy = "companyAdmin")
//         @JsonIgnore
//         private Set<requestTraining> requestTraining;


}
>>>>>>> test
