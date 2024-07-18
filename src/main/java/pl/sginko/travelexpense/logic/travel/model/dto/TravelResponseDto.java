package pl.sginko.travelexpense.logic.travel.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.diet.model.dto.DietResponseDto;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.logic.transport.model.dto.TransportCostResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
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
    private BigDecimal otherExpenses;
    private BigDecimal totalAmount;
    private BigDecimal advancePayment;
    private DietResponseDto dietResponse;
    private OvernightStayResponseDto overnightStayResponseDto;
    private TransportCostResponseDto transportCostResponseDto;
}
