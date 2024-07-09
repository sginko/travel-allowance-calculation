package pl.sginko.travelexpense.logic.overnightStay.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor //(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class OvernightStayDto {
    @NotNull(message = "Number of overnight stay without invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay without invoice cannot be negative")
    private Integer inputQuantityOfOvernightStayWithoutInvoice = 0;

    @NotNull(message = "overnightStayWithoutInvoice cannot be null")
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;

    @NotNull(message = "Number of overnight stay with invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay with invoice cannot be negative")
    private Integer inputQuantityOfOvernightStayWithInvoice = 0;

    @NotNull(message = "overnightStayWithInvoice cannot be null")
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;

    @NotNull(message = "quantityOfOvernightStay cannot be null")
    @Min(value = 0, message = "overnightStayWithInvoice cannot be negative")
    private Integer quantityOfOvernightStay;
}
