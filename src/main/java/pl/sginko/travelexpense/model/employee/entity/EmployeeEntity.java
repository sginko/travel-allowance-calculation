package pl.sginko.travelexpense.model.employee.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long pesel;
    private String firstName;
    private String secondName;
    private String position;

    public EmployeeEntity(Long pesel, String firstName, String secondName, String position) {
        this.pesel = pesel;
        this.firstName = firstName;
        this.secondName = secondName;
        this.position = position;
    }
}
