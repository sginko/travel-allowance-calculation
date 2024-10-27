//package pl.sginko.travelexpense.logic.user.model.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@Entity
//@Table(name = "users")
//public class UserEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotNull(message = "Pessel cannot be null")
//    @Min(value = 11, message = "Pessel cannot be negative")
//    @Column(nullable = false, updatable = false, unique = true)
//    private Long pesel;
//
//    @NotBlank(message = "First name cannot be blank")
//    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
//    @Column(nullable = false)
//    private String firstName;
//
//    @NotBlank(message = "Second name cannot be blank")
//    @Size(min = 2, max = 50, message = "Second name should be between 2 and 50 characters")
//    @Column(nullable = false)
//    private String secondName;
//
//    @NotBlank(message = "Position cannot be blank")
//    @Size(min = 2, max = 50, message = "Position should be between 2 and 50 characters")
//    @Column(nullable = false)
//    private String position;
//
//    public UserEntity(Long pesel, String firstName, String secondName, String position) {
//        this.pesel = pesel;
//        this.firstName = firstName;
//        this.secondName = secondName;
//        this.position = position;
//    }
//}
