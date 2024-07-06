package pl.sginko.travelexpense.logic.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sginko.travelexpense.logic.employee.entity.EmployeeEntity;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class TravelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

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

    @NotNull(message = "Number of breakfasts cannot be null")
    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    @Column(nullable = false)
    private Integer numberOfBreakfasts;

    @NotNull(message = "Number of lunches cannot be null")
    @Min(value = 0, message = "Number of lunches cannot be negative")
    @Column(nullable = false)
    private Integer numberOfLunches;

    @NotNull(message = "Number of dinners cannot be null")
    @Min(value = 0, message = "Number of dinners cannot be negative")
    @Column(nullable = false)
    private Integer numberOfDinners;

    @NotNull(message = "Daily allowance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Daily allowance must be non-negative")
    @Column(nullable = false)
    private BigDecimal dailyAllowance = BigDecimal.valueOf(45);

    @NotNull(message = "Total amount cannot be null")
    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull(message = "Diet amount cannot be null")
    @Column(nullable = false)
    private BigDecimal dietAmount;

    @NotNull(message = "Food amount cannot be null")
    @Column(nullable = false)
    private BigDecimal foodAmount = BigDecimal.ZERO;

    @NotNull(message = "Number of overnight stay with invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay with invoice cannot be negative")
    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @NotNull(message = "Field Amount Overnight Stay With Invoice cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Field Amount Overnight Stay With Invoice must be non-negative")
    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

    @NotNull(message = "Number of overnight stay without invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay without invoice cannot be negative")
    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @NotNull(message = "Field Amount Overnight Stay Without Invoice cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Field Amount Overnight Stay Without Invoice must be non-negative")
    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer totalInputQuantityOfOvernightStay = 0;

    @Column(nullable = false)
    private Integer quantityOfOvernightStay = 0;

    @Column(nullable = false)
    private BigDecimal overnightStayAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal advancePayment = BigDecimal.ZERO;

    public TravelEntity(String fromCity, String toCity, LocalDate startDate, LocalTime startTime,
                        LocalDate endDate, LocalTime endTime, Integer numberOfBreakfasts,
                        Integer numberOfLunches, Integer numberOfDinners, EmployeeEntity employeeEntity,
                        Integer inputQuantityOfOvernightStayWithoutInvoice, Integer inputQuantityOfOvernightStayWithInvoice,
                        BigDecimal amountOfTotalOvernightsStayWithInvoice,
                        BigDecimal advancePayment) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.employeeEntity = employeeEntity;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
        this.advancePayment = advancePayment;
        validateDates();
    }

    private void validateDates() {
        if (endDate.isBefore(startDate) || (endDate.isEqual(startDate) && endTime.isBefore(startTime))) {
            throw new TravelException("End date/time cannot be before start date/time");
        }
    }

    public void updateTotalAmount() {
        this.totalAmount = (this.dietAmount.add(this.foodAmount).add(this.overnightStayAmount)).subtract(advancePayment);
    }
}
