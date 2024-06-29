package pl.sginko.travelexpense.model.overnightStayExpenses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "overnight_stay")
public class OvernightStayExpensesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Number of overnight stay with invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay with invoice cannot be negative")
    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @NotNull(message = "Field Amount Overnight Stay With Invoice cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Field Amount Overnight Stay With Invoice must be non-negative")
    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

    @NotNull(message = "Number of overnight stay without invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay without invoice cannot be negative")
    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @NotNull(message = "Field Amount Overnight Stay Without Invoice cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Field Amount Overnight Stay Without Invoice must be non-negative")
    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer totalInputQuantityOfOvernightStay;

    @Column(nullable = false)
    private Integer quantityOfOvernightStay;

    @Column(nullable = false)
    private BigDecimal overnightStayAmount = BigDecimal.ZERO;

    public OvernightStayExpensesEntity(Integer inputQuantityOfOvernightStayWithoutInvoice,
                                       Integer inputQuantityOfOvernightStayWithInvoice,
                                       BigDecimal amountOfTotalOvernightsStayWithInvoice) {
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
    }
}
