package pl.sginko.travelexpense.model.travel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.model.employee.entity.EmployeeEntity;
import pl.sginko.travelexpense.model.travel.TravelException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private BigDecimal dailyAllowance;

    @NotNull(message = "Total amount cannot be null")
    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull(message = "Diet amount cannot be null")
    @Column(nullable = false)
    private BigDecimal dietAmount = BigDecimal.ZERO;

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
    private Integer totalInputQuantityOfOvernightStay;

    @Column(nullable = false)
    private Integer quantityOfOvernightStay;

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
        this.dailyAllowance = BigDecimal.valueOf(45);
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
        this.advancePayment = advancePayment;
        validateDates();
    }

    public void calculateDietAmount() {
        long hoursInTravel = Duration.between(startTime.atDate(startDate), endTime.atDate(endDate)).toHours();
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.50));

        if (hoursInTravel <= 24) {
            if (hoursInTravel < 8) {
                this.dietAmount = BigDecimal.ZERO;
            } else if (hoursInTravel < 12) {
                this.dietAmount = fiftyPercentOfDailyAllowance;
            } else {
                this.dietAmount = dailyAllowance;
            }
        } else {
            long fullDays = hoursInTravel / 24;
            long remainingHours = hoursInTravel % 24;
            BigDecimal totalAmountForFullDays = dailyAllowance.multiply(BigDecimal.valueOf(fullDays));

            if (remainingHours == 0) {
                this.dietAmount = totalAmountForFullDays.add(BigDecimal.ZERO);
            } else if (remainingHours < 8) {
                this.dietAmount = totalAmountForFullDays.add(fiftyPercentOfDailyAllowance);
            } else {
                this.dietAmount = totalAmountForFullDays.add(dailyAllowance);
            }
        }
        updateTotalAmount();
    }

    public void calculateFoodAmount() {
        BigDecimal totalFoodExpenses = BigDecimal.ZERO;
        BigDecimal fiftyPercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.5));
        BigDecimal twentyFivePercentOfDailyAllowance = dailyAllowance.multiply(BigDecimal.valueOf(0.25));

        BigDecimal breakfastCost = BigDecimal.valueOf(numberOfBreakfasts).multiply(twentyFivePercentOfDailyAllowance);
        BigDecimal lunchCost = BigDecimal.valueOf(numberOfLunches).multiply(fiftyPercentOfDailyAllowance);
        BigDecimal dinnerCost = BigDecimal.valueOf(numberOfDinners).multiply(twentyFivePercentOfDailyAllowance);

        this.foodAmount = totalFoodExpenses.add(breakfastCost).add(lunchCost).add(dinnerCost).negate();
        updateTotalAmount();
    }

    public void calculateOvernightStayAmount() {
        quantityOfOvernightStay = getTotalQuantityOfNight();

        BigDecimal oneNightWithInvoice = dailyAllowance.multiply(BigDecimal.valueOf(20));
        BigDecimal oneNightWithoutInvoice = dailyAllowance.multiply(BigDecimal.valueOf(1.5));

        if (inputQuantityOfOvernightStayWithoutInvoice > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        } else {
            amountOfTotalOvernightsStayWithoutInvoice = oneNightWithoutInvoice.multiply(BigDecimal.valueOf(inputQuantityOfOvernightStayWithoutInvoice));
        }

        if (inputQuantityOfOvernightStayWithInvoice > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        }

        if ((inputQuantityOfOvernightStayWithInvoice + inputQuantityOfOvernightStayWithoutInvoice) > quantityOfOvernightStay) {
            throw new TravelException("Total input numbers of overnight stay more than total overnight stay");
        }
        this.totalInputQuantityOfOvernightStay = inputQuantityOfOvernightStayWithInvoice + inputQuantityOfOvernightStayWithoutInvoice;
        this.overnightStayAmount = amountOfTotalOvernightsStayWithoutInvoice.add(amountOfTotalOvernightsStayWithInvoice);

        updateTotalAmount();
    }

    private int getTotalQuantityOfNight() {
        int night = 0;
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        while (startDateTime.isBefore(endDateTime)) {
            LocalDateTime endOfCurrentNight = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);

            if (endDateTime.isBefore(endOfCurrentNight)) {
                endOfCurrentNight = endDateTime;
            }

            LocalDateTime startOfCurrentNight = startDateTime.withHour(21).withMinute(0).withSecond(0);

            if (startDateTime.isAfter(startOfCurrentNight)) {
                startOfCurrentNight = startDateTime;
            }

            if (Duration.between(startOfCurrentNight, endOfCurrentNight).toHours() >= 6) {
                night++;
            }

            startDateTime = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
        }
        return night;
    }

    private void validateDates() {
        if (endDate.isBefore(startDate) || (endDate.isEqual(startDate) && endTime.isBefore(startTime))) {
            throw new TravelException("End date/time cannot be before start date/time");
        }
    }

    private void updateTotalAmount() {
        this.totalAmount = (this.dietAmount.add(this.foodAmount).add(this.overnightStayAmount)).subtract(advancePayment);
    }
}
