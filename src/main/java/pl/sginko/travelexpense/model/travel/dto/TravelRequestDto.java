package pl.sginko.travelexpense.model.travel.dto;

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

    @NotNull(message = "overnightStayWithInvoice cannot be null")
    @Min(value = 0, message = "overnightStayWithInvoice cannot be negative")
    private BigDecimal overnightStayWithInvoice;

    @NotNull(message = "overnightStayWithoutInvoice cannot be null")
    @Min(value = 0, message = "overnightStayWithoutInvoice cannot be negative")
    private BigDecimal overnightStayWithoutInvoice;

    @NotNull(message = "advancePayment cannot be null")
    @Min(value = 0, message = "advancePayment cannot be negative")
    private BigDecimal advancePayment;


    public TravelRequestDto(Long pesel, String fromCity, String toCity, LocalDate startDate, LocalTime startTime,
                            LocalDate endDate, LocalTime endTime, Integer numberOfBreakfasts, Integer numberOfLunches,
                            Integer numberOfDinners, BigDecimal overnightStayWithInvoice, BigDecimal overnightStayWithoutInvoice,
                            BigDecimal advancePayment) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.pesel = pesel;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.numberOfBreakfasts = numberOfBreakfasts;
        this.numberOfLunches = numberOfLunches;
        this.numberOfDinners = numberOfDinners;
        this.overnightStayWithInvoice = overnightStayWithInvoice;
        this.overnightStayWithoutInvoice = overnightStayWithoutInvoice;
        this.advancePayment = advancePayment;
    }
}
