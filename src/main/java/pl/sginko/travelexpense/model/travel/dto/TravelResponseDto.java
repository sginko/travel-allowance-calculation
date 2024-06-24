package pl.sginko.travelexpense.model.travel.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelResponseDto {
    private Long id;
    private Long pesel;
    private String fromCity;
    private String toCity;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Integer numberOfBreakfasts;
    private Integer numberOfLunches;
    private Integer numberOfDinners;
    private BigDecimal totalAmount;
    private BigDecimal dietAmount;
    private BigDecimal foodAmount;
    private BigDecimal overnightStayWithInvoice;
    private BigDecimal overnightStayWithoutInvoice;
    private BigDecimal totalOvernightStay;
    private BigDecimal advancePayment;

    public TravelResponseDto(Long id, String fromCity, String toCity, LocalDate startDate, LocalTime startTime,
                             LocalDate endDate, LocalTime endTime, Integer numberOfBreakfasts, Integer numberOfLunches,
                             Integer numberOfDinners, BigDecimal totalAmount, BigDecimal dietAmount,
                             BigDecimal foodAmount, Long pesel, BigDecimal overnightStayWithInvoice,
                             BigDecimal overnightStayWithoutInvoice, BigDecimal totalOvernightStay,
                             BigDecimal advancePayment) {
        this.id = id;
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
        this.totalAmount = totalAmount;
        this.dietAmount = dietAmount;
        this.foodAmount = foodAmount;
        this.overnightStayWithInvoice = overnightStayWithInvoice;
        this.overnightStayWithoutInvoice = overnightStayWithoutInvoice;
        this.totalOvernightStay = totalOvernightStay;
        this.advancePayment = advancePayment;
    }
}
