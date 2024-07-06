package pl.sginko.travelexpense.logic.travel.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.diet.model.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayResponseDto;

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
    private BigDecimal totalAmount;
    private BigDecimal advancePayment;
    private DietResponseDto dietResponse;
    private OvernightStayResponseDto overnightStayResponseDto;

    public TravelResponseDto(Long id, Long pesel, String fromCity, String toCity, LocalDate startDate, LocalTime startTime,
                             LocalDate endDate, LocalTime endTime, BigDecimal totalAmount, BigDecimal advancePayment,
                             DietResponseDto dietResponse, OvernightStayResponseDto overnightStayResponseDto) {
        this.id = id;
        this.pesel = pesel;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.totalAmount = totalAmount;
        this.advancePayment = advancePayment;
        this.dietResponse = dietResponse;
        this.overnightStayResponseDto = overnightStayResponseDto;
    }
}
