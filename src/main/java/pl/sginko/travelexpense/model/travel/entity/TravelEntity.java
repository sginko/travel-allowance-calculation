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

//    @NotNull(message = "Can go home every day flag cannot be null")
//    @Column(nullable = false)
//    private boolean canGoHomeEveryDay;

//    @NotNull(message = "Free overnight stay provided flag cannot be null")
//    @Column(nullable = false)
//    private boolean freeOvernightStayProvided;

//    @NotNull(message = "Hotel invoice provided flag cannot be null")
//    @Column(nullable = false)
//    private boolean hotelInvoiceProvided;

    @NotNull(message = "Invoice amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Invoice amount must be non-negative")
    @Column(nullable = false)
    private BigDecimal hotelInvoiceProvided;

    @NotNull(message = "Overnight stay amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Overnight stay amount must be non-negative")
    @Column(nullable = false)
    private BigDecimal overnightStay;

//    @NotNull(message = "Confirmed overnight stay amount cannot be null")
//    @DecimalMin(value = "0.0", inclusive = true, message = "Confirmed overnight stay amount must be non-negative")
//    @Column(nullable = false)
//    private BigDecimal confirmedOvernightStay = BigDecimal.ZERO;
//
//    private BigDecimal invoiceAmount;

    public TravelEntity(String fromCity, String toCity, LocalDate startDate, LocalTime startTime,
                        LocalDate endDate, LocalTime endTime, Integer numberOfBreakfasts,
                        Integer numberOfLunches, Integer numberOfDinners, EmployeeEntity employeeEntity,
                        boolean canGoHomeEveryDay, boolean freeOvernightStayProvided, BigDecimal hotelInvoiceProvided,
                        BigDecimal overnightStay) {
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
//        this.canGoHomeEveryDay = canGoHomeEveryDay;
//        this.freeOvernightStayProvided = freeOvernightStayProvided;
        this.hotelInvoiceProvided = hotelInvoiceProvided;
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

            if (remainingHours < 8) {
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

    public void calculateOvernightStay() {
//        if (canGoHomeEveryDay || freeOvernightStayProvided) {
//            this.overnightStay = BigDecimal.ZERO;
//        } else {
        long nights = 0;

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        while (startDateTime.isBefore(endDateTime)) {
            LocalDateTime nextDay = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);

            LocalDateTime startOfNight = startDateTime.withHour(21).withMinute(0).withSecond(0);
            if (startDateTime.isBefore(startOfNight)) {
                startOfNight = startDateTime;
            }

            LocalDateTime endOfNight = nextDay.withHour(7).withMinute(0).withSecond(0);
            if (endDateTime.isBefore(endOfNight)) {
                endOfNight = endDateTime;
            }
            if (Duration.between(startOfNight, endOfNight).toHours() >= 6) {
                nights++;
            }
            startDateTime = nextDay;
        }

        BigDecimal maxAmountPerNight = dailyAllowance.multiply(BigDecimal.valueOf(20));
        BigDecimal ryczaltPerNight = dailyAllowance.multiply(BigDecimal.valueOf(1.5));

        if (hotelInvoiceProvided) {
            // Убедитесь, что invoiceAmount не превышает maxAmountPerNight за одну ночь
            BigDecimal totalInvoiceAmount = invoiceAmount.min(maxAmountPerNight).multiply(BigDecimal.valueOf(nights));
            this.overnightStay = totalInvoiceAmount;
        } else {
            // Сумма за ночлег рассчитывается автоматически
            BigDecimal totalRyczalt = ryczaltPerNight.multiply(BigDecimal.valueOf(nights));
            this.overnightStay = totalRyczalt;
        }
//    }

    updateTotalAmount();
}

//    public void calculateOvernightStay() {
//        if (canGoHomeEveryDay || freeOvernightStayProvided) {
//            this.overnightStay = BigDecimal.ZERO;
//        } else {
//            long nights = 0;
//
//            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
//            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
//
//            while (startDateTime.isBefore(endDateTime)) {
//                LocalDateTime nextDay = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
//
//                LocalDateTime startOfNight = startDateTime.withHour(21).withMinute(0).withSecond(0);
//                if (startDateTime.isBefore(startOfNight)) {
//                    startOfNight = startDateTime;
//                }
//
//                LocalDateTime endOfNight = nextDay.withHour(7).withMinute(0).withSecond(0);
//                if (endDateTime.isBefore(endOfNight)) {
//                    endOfNight = endDateTime;
//                }
//
//                if (Duration.between(startOfNight, endOfNight).toHours() >= 6) {
//                    nights++;
//                }
//
//                startDateTime = nextDay;
//            }
//
//            BigDecimal maxAmountPerNight = dailyAllowance.multiply(BigDecimal.valueOf(20));
//            BigDecimal ryczaltPerNight = dailyAllowance.multiply(BigDecimal.valueOf(1.5));
//
//            if (hotelInvoiceProvided) {
//                // Убедитесь, что invoiceAmount не превышает maxAmountPerNight за одну ночь
//                BigDecimal totalInvoiceAmount = invoiceAmount.min(maxAmountPerNight).multiply(BigDecimal.valueOf(nights));
//                this.overnightStay = totalInvoiceAmount;
//            } else {
//                // Сумма за ночлег рассчитывается автоматически
//                BigDecimal totalRyczalt = ryczaltPerNight.multiply(BigDecimal.valueOf(nights));
//                this.overnightStay = totalRyczalt;
//            }
//        }
//        updateTotalAmount();
//    }

private void validateDates() {
    if (endDate.isBefore(startDate) || (endDate.isEqual(startDate) && endTime.isBefore(startTime))) {
        throw new TravelException("End date/time cannot be before start date/time");
    }
}

private void updateTotalAmount() {
    this.totalAmount = this.dietAmount.add(this.foodAmount).add(this.overnightStay);
}
}
