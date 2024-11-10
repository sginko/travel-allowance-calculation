package pl.sginko.travelexpense.domen.travelexpense.travel.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.domen.travelexpense.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.domen.travelexpense.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.domen.travelexpense.transportCost.dto.TransportCostResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelResponseDto {
    private UUID techId;
    private String email;
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
