package pl.sginko.travelexpense.logic.overnightStay.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

import java.math.BigDecimal;

@Getter
@Setter
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

    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal overnightStayAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer totalInputQuantityOfOvernightStay;

    @Column(nullable = false)
    private Integer quantityOfOvernightStay;

    public OvernightStayEntity(TravelEntity travelEntity, Integer inputQuantityOfOvernightStayWithoutInvoice,
                               Integer inputQuantityOfOvernightStayWithInvoice, BigDecimal amountOfTotalOvernightsStayWithInvoice) {
        this.travelEntity = travelEntity;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
        this.quantityOfOvernightStay = getQuantityOfOvernightStay();
    }
}