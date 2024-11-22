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
    private String fullName;
    private String fromCity;
    private String toCity;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private int countBreakfast;
    private int countLunch;
    private int countDinner;
    private BigDecimal totalAmount;
    private String totalAmountWords;
    private BigDecimal dietAmount;
    private BigDecimal foodAmount;
    private BigDecimal overnightStayWithInvoice;
    private BigDecimal overnightStayWithoutInvoice;
    private BigDecimal advancePayment;
    private String advancePaymentWords;
    private BigDecimal undocumentedLocalTransportCost;
    private BigDecimal documentedLocalTransportCost;
    private String meansOfTransport;
    private BigDecimal totalCostOfTravelByOwnAndPublicTransport;
    private BigDecimal transportCostAmount;
    private BigDecimal otherExpenses;
}
