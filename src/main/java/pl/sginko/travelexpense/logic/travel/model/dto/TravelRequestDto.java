package pl.sginko.travelexpense.logic.travel.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelRequestDto {

    @NotNull(message = "PESEL cannot be null")
    private Long pesel;

    @NotBlank(message = "From city cannot be blank")
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    private String fromCity;

    @NotBlank(message = "To city cannot be blank")
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    private String toCity;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

    @NotNull(message = "Number of breakfasts cannot be null")
    @Min(value = 0, message = "Number of breakfasts cannot be negative")
    private Integer numberOfBreakfasts;

    @NotNull(message = "Number of lunches cannot be null")
    @Min(value = 0, message = "Number of lunches cannot be negative")
    private Integer numberOfLunches;

    @NotNull(message = "Number of dinners cannot be null")
    @Min(value = 0, message = "Number of dinners cannot be negative")
    private Integer numberOfDinners;

    @NotNull(message = "Number of overnight stay without invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay without invoice cannot be negative")
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @NotNull(message = "overnightStayWithoutInvoice cannot be null")
    @Min(value = 0, message = "overnightStayWithoutInvoice cannot be negative")
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;

    @NotNull(message = "Number of overnight stay with invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay with invoice cannot be negative")
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @NotNull(message = "overnightStayWithoutInvoice cannot be null")
    @Min(value = 0, message = "overnightStayWithoutInvoice cannot be negative")
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;

    @NotNull(message = "advancePayment cannot be null")
    @Min(value = 0, message = "advancePayment cannot be negative")
    private BigDecimal advancePayment;

    public TravelRequestDto(Long pesel, String fromCity, String toCity, LocalDate startDate, LocalTime startTime,
                            LocalDate endDate, LocalTime endTime, Integer numberOfBreakfasts, Integer numberOfLunches,
                            Integer numberOfDinners, Integer inputQuantityOfOvernightStayWithoutInvoice,
                            Integer inputQuantityOfOvernightStayWithInvoice,
                            BigDecimal amountOfTotalOvernightsStayWithInvoice, BigDecimal advancePayment) {
        this.pesel = pesel;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
        this.advancePayment = advancePayment;
    }
}
