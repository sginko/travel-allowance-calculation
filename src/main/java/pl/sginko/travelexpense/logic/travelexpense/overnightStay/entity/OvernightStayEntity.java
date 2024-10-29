package pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.exception.OvernightStayException;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "overnight_stay")
public class OvernightStayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @Min(value = 0, message = "Number of overnight stay without invoice cannot be negative")
    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;

    @Min(value = 0, message = "Number of overnight stay with invoice cannot be negative")
    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;

    @Column(nullable = false)
    private BigDecimal overnightStayAmount;

    @Column(nullable = false)
    private Integer totalInputQuantityOfOvernightStay;

    @Column(nullable = false)
    private Integer quantityOfOvernightStay;

    @Column(nullable = false)
    private Boolean isInvoiceAmountGreaterAllowed;

    public OvernightStayEntity(TravelEntity travelEntity, Integer inputQuantityOfOvernightStayWithoutInvoice,
                               Integer inputQuantityOfOvernightStayWithInvoice, BigDecimal amountOfTotalOvernightsStayWithInvoice,
                               Boolean isInvoiceAmountGreaterAllowed) {
        this.travelEntity = travelEntity;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice != null ? inputQuantityOfOvernightStayWithoutInvoice : 0;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice != null ? inputQuantityOfOvernightStayWithInvoice : 0;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice != null ? amountOfTotalOvernightsStayWithInvoice : BigDecimal.ZERO;
        this.isInvoiceAmountGreaterAllowed = isInvoiceAmountGreaterAllowed != null ? isInvoiceAmountGreaterAllowed : false;
    }

    public void calculateOvernightStayAmounts() {
        this.quantityOfOvernightStay = calculateQuantityOfOvernightStay();
        this.totalInputQuantityOfOvernightStay = inputQuantityOfOvernightStayWithInvoice + inputQuantityOfOvernightStayWithoutInvoice;
        validateInputNights();

        this.amountOfTotalOvernightsStayWithoutInvoice = calculateAmountOfOvernightStayWithoutInvoice();
        this.overnightStayAmount = amountOfTotalOvernightsStayWithoutInvoice.add(amountOfTotalOvernightsStayWithInvoice);
    }

    private int calculateQuantityOfOvernightStay() {
        long hoursInTravel = travelEntity.getDurationInHours();
        int nights = (int) (hoursInTravel / 24);

        if (hoursInTravel % 24 >= 6) {
            nights += 1;
        }

        return nights;
    }

    private void validateInputNights() {
        if (totalInputQuantityOfOvernightStay > quantityOfOvernightStay) {
            throw new OvernightStayException("The number of nights entered for overnight stay is greater than the number of nights on the trip");
        }
    }

    private BigDecimal calculateAmountOfOvernightStayWithoutInvoice() {
        BigDecimal oneNightWithoutInvoice = travelEntity.getDietEntity().getDailyAllowance().multiply(BigDecimal.valueOf(1.5));
        return oneNightWithoutInvoice.multiply(BigDecimal.valueOf(inputQuantityOfOvernightStayWithoutInvoice));
    }
}
