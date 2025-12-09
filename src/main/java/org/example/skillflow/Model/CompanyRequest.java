package org.example.skillflow.Model;
import jakarta.persistence.*;
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
public class CompanyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , length = 200)
    private String fullName;

    @Column(unique = true , nullable = false)
    private String email;

    @Column(nullable = false , length = 200)
    private String companyName;

    @Column(nullable = false , length = 10)
    private String recordNumber;

    @Column(nullable = false , length = 50)
    private String country;

    @Column(nullable = false , length = 40)
    private String industry;


    @Column(columnDefinition = "varchar(20) default 'PENDING'")
    private String status;

    private Integer checkedByAdmin;

    private LocalDate requestDate;

    private LocalDate endDate;


}
