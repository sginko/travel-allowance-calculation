package pl.sginko.travelexpense.domain.travelReport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayEditDto;
import pl.sginko.travelexpense.domain.travelReport.exception.OvernightStayException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "overnight_stay")
public class OvernightStayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "overnightStayEntity")
    private TravelReportEntity travelReportEntity;

    @Min(value = 0, message = "Number of overnight stay without invoice cannot be negative")
    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @Column(nullable = false)
    private BigDecimal totalAmountOfOvernightsStayWithoutInvoice;

    @Min(value = 0, message = "Number of overnight stay with invoice cannot be negative")
    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @Column(nullable = false)
    private BigDecimal totalAmountOfOvernightsStayWithInvoice;

    @Column(nullable = false)
    private BigDecimal overnightStayAmount;

    @Column(nullable = false)
    private Integer totalInputQuantityOfOvernightStay;

    @Column(nullable = false)
    private Integer quantityOfOvernightStay;

    @Column(nullable = false)
    private Boolean isInvoiceAmountGreaterAllowed;

    public OvernightStayEntity(TravelReportEntity travelReportEntity, Integer inputQuantityOfOvernightStayWithoutInvoice,
                               Integer inputQuantityOfOvernightStayWithInvoice, BigDecimal totalAmountOfOvernightsStayWithInvoice,
                               Boolean isInvoiceAmountGreaterAllowed) {
        this.travelReportEntity = travelReportEntity;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice != null ? inputQuantityOfOvernightStayWithoutInvoice : 0;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice != null ? inputQuantityOfOvernightStayWithInvoice : 0;
        this.totalAmountOfOvernightsStayWithInvoice = totalAmountOfOvernightsStayWithInvoice != null ? totalAmountOfOvernightsStayWithInvoice : BigDecimal.ZERO;
        this.isInvoiceAmountGreaterAllowed = isInvoiceAmountGreaterAllowed != null ? isInvoiceAmountGreaterAllowed : false;
        this.quantityOfOvernightStay = calculateQuantityOfOvernightStay();
        this.totalInputQuantityOfOvernightStay = calculateTotalInputQuantityOfOvernightStay();
        this.totalAmountOfOvernightsStayWithoutInvoice = calculateTotalAmountOfOvernightStayWithoutInvoice();
        this.overnightStayAmount = calculateOvernightStayAmount();
    }

    public BigDecimal calculateOvernightStayAmount() {
        return calculateTotalAmountOfOvernightStayWithInvoice().add(totalAmountOfOvernightsStayWithoutInvoice);
    }

    public void updateOvernightStayDetails(OvernightStayEditDto overnightStayEditDto) {
        this.inputQuantityOfOvernightStayWithoutInvoice = overnightStayEditDto.getInputQuantityOfOvernightStayWithoutInvoice();
        this.inputQuantityOfOvernightStayWithInvoice = overnightStayEditDto.getInputQuantityOfOvernightStayWithInvoice();
        this.totalAmountOfOvernightsStayWithInvoice = overnightStayEditDto.getTotalAmountOfOvernightsStayWithInvoice();
        this.isInvoiceAmountGreaterAllowed = overnightStayEditDto.getIsInvoiceAmountGreaterAllowed();

        this.totalInputQuantityOfOvernightStay = calculateTotalInputQuantityOfOvernightStay();
        this.quantityOfOvernightStay = calculateQuantityOfOvernightStay();
        this.totalAmountOfOvernightsStayWithoutInvoice = calculateTotalAmountOfOvernightStayWithoutInvoice();
        this.overnightStayAmount = calculateOvernightStayAmount();
    }

    private BigDecimal calculateTotalAmountOfOvernightStayWithoutInvoice() {
        checkQuantityOfNightInTravel();
        BigDecimal dailyAllowance = travelReportEntity.getDietEntity().getDailyAllowance();
        BigDecimal oneNightWithoutInvoice = dailyAllowance.multiply(BigDecimal.valueOf(1.5));
        return oneNightWithoutInvoice.multiply(BigDecimal.valueOf(inputQuantityOfOvernightStayWithoutInvoice));
    }

    private BigDecimal calculateTotalAmountOfOvernightStayWithInvoice() {
        checkQuantityOfNightInTravel();
        BigDecimal dailyAllowance = travelReportEntity.getDietEntity().getDailyAllowance();

        if (!isInvoiceAmountGreaterAllowed) {
            BigDecimal maxAmountForOneNightWithInvoice = dailyAllowance.multiply(BigDecimal.valueOf(20));
            if (totalAmountOfOvernightsStayWithInvoice.compareTo(maxAmountForOneNightWithInvoice) > 0) {
                throw new OvernightStayException("Total amount exceeds the maximum allowable amount");
            }
        }
        return totalAmountOfOvernightsStayWithInvoice;
    }

    private Integer calculateQuantityOfOvernightStay() {
        Integer quantity = 0;
        LocalDateTime startDateTime = LocalDateTime.of(travelReportEntity.getStartDate(), travelReportEntity.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(travelReportEntity.getEndDate(), travelReportEntity.getEndTime());

        while (startDateTime.isBefore(endDateTime)) {
            LocalDateTime endOfCurrentNight = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
            if (endDateTime.isBefore(endOfCurrentNight)) {
                endOfCurrentNight = endDateTime;
            }

            LocalDateTime startOfCurrentNight = startDateTime.withHour(21).withMinute(0).withSecond(0);
            if (startDateTime.isAfter(startOfCurrentNight)) {
                startOfCurrentNight = startDateTime;
            }

            if (Duration.between(startOfCurrentNight, endOfCurrentNight).toHours() >= 6) {
                quantity++;
            }

            startDateTime = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
        }
        return quantity;
    }

    private Integer calculateTotalInputQuantityOfOvernightStay() {
        return inputQuantityOfOvernightStayWithInvoice + inputQuantityOfOvernightStayWithoutInvoice;
    }

    private void checkQuantityOfNightInTravel() {
        Integer totalInputQuantity = calculateTotalInputQuantityOfOvernightStay();
        Integer calculatedQuantityOfOvernightStay = calculateQuantityOfOvernightStay();

        if (inputQuantityOfOvernightStayWithInvoice > calculatedQuantityOfOvernightStay ||
                inputQuantityOfOvernightStayWithoutInvoice > calculatedQuantityOfOvernightStay ||
                totalInputQuantity > calculatedQuantityOfOvernightStay) {
            throw new OvernightStayException("The number of nights entered for overnight stay is greater than the number of nights on the trip");
        }
    }
}
