package pl.sginko.travelexpense.model.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private Long pesel;

    @NotBlank
    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50, message = "Second name should be between 2 and 50 characters")
    @Column(nullable = false)
    private String secondName;

    @NotBlank
    @Size(min = 2, max = 50, message = "Position should be between 2 and 50 characters")
    @Column(nullable = false)
    private String position;

    public EmployeeEntity(Long pesel, String firstName, String secondName, String position) {
        this.pesel = pesel;
        this.firstName = firstName;
        this.secondName = secondName;
        this.position = position;
    }
}
