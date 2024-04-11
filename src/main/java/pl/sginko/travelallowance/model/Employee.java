package pl.sginko.travelallowance.model;

import jakarta.persistence.Entity;

@Entity
class Employee {
    private Long id;
    private Integer pesel;
    private String firstName;
    private String secondName;

}
