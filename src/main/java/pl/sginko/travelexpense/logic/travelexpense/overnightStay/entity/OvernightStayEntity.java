package pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
        this.isInvoiceAmountGreaterAllowed = isInvoiceAmountGreaterAllowed;
    }

    public void updateQuantityOfOvernightStay(Integer quantityOfOvernightStay) {
        this.quantityOfOvernightStay = quantityOfOvernightStay;
    }

    public void updateTotalInputQuantityOfOvernightStay(Integer totalInputQuantityOfOvernightStay) {
        this.totalInputQuantityOfOvernightStay = totalInputQuantityOfOvernightStay;
    }

    public void updateAmountOfTotalOvernightsStayWithoutInvoice(BigDecimal amountOfTotalOvernightsStayWithoutInvoice) {
        this.amountOfTotalOvernightsStayWithoutInvoice = amountOfTotalOvernightsStayWithoutInvoice;
    }

    public void updateOvernightStayAmount(BigDecimal overnightStayAmount) {
        this.overnightStayAmount = overnightStayAmount;
    }
}
