package pl.sginko.travelexpense.model.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.model.dietExpenses.entity.DietExpensesEntity;
import pl.sginko.travelexpense.model.employee.entity.EmployeeEntity;
import pl.sginko.travelexpense.model.travel.TravelException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class TravelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private DietExpensesEntity dietExpensesEntity;

    @NotBlank
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    @Column(nullable = false)
    private String fromCity;

    @NotBlank
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    @Column(nullable = false)
    private String toCity;

    @NotNull(message = "The Date field cannot be empty")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "The Time field cannot be empty")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "The Date field cannot be empty")
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull(message = "The Time field cannot be empty")
    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private BigDecimal advancePayment = BigDecimal.ZERO;

    @Column(nullable = false)
    private Long hoursInTravel;

    @NotNull(message = "Total amount cannot be null")
    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public TravelEntity(EmployeeEntity employeeEntity, String fromCity, String toCity, LocalDate startDate,
                        LocalTime startTime, LocalDate endDate, LocalTime endTime, BigDecimal advancePayment) {
        this.employeeEntity = employeeEntity;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.advancePayment = advancePayment;
        validateDates();
    }

    private void validateDates() {
        if (endDate.isBefore(startDate) || (endDate.isEqual(startDate) && endTime.isBefore(startTime))) {
            throw new TravelException("End date/time cannot be before start date/time");
        }
    }
}
