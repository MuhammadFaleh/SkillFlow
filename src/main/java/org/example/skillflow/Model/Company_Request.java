package org.example.skillflow.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Company_Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "fullName must be not empty")
    @Column(nullable = false , length = 200)
    private String fullName;

    @NotEmpty(message = "CompanyName must be not empty")
    @Column(nullable = false , length = 200)
    private String companyName;

    @NotEmpty(message = "record number must be not empty")
    @Column(nullable = false , length = 10)
    private String recordNumber;

    @NotEmpty(message = "country must be not empty")
    @Column(nullable = false , length = 50)
    private String county;

    @NotEmpty(message = "industry must be not empty")
    @Column(nullable = false , length = 40)
    private String industry;


    @Column(columnDefinition = "varchar(20) default 'PENDING'")
    private String status;


    private LocalDate requestDate;


    private LocalDate endDate;


}
