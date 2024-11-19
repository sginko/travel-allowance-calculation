package pl.sginko.travelexpense.domain.travelReport.dto.travelReport;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.domain.travelReport.dto.diet.DietResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelReportResponseDto {
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
