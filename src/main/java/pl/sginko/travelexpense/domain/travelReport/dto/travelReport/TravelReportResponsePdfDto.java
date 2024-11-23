package pl.sginko.travelexpense.domain.travelReport.dto.travelReport;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelReportResponsePdfDto {
    private String name;
    private String surname;
    private String fromCity;
    private String toCity;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private int numberOfBreakfasts;
    private int numberOfLunches;
    private int numberOfDinners;
    private BigDecimal totalAmount;
    private BigDecimal dietAmount;
    private BigDecimal foodAmount;
    private BigDecimal totalAmountOfOvernightsStayWithInvoice;
    private BigDecimal totalAmountOfOvernightsStayWithoutInvoice;
    private BigDecimal advancePayment;
    private BigDecimal undocumentedLocalTransportCost;
    private BigDecimal documentedLocalTransportCost;
    private String meansOfTransport;
    private BigDecimal totalCostOfTravelByOwnAndPublicTransport;
    private BigDecimal transportCostAmount;
    private BigDecimal otherExpenses;
}
