package pl.sginko.travelexpense.logic.overnightStay.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OvernightStayDto {
    @NotNull(message = "Number of overnight stay without invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay without invoice cannot be negative")
    private Integer inputQuantityOfOvernightStayWithoutInvoice = 0;

    @NotNull(message = "overnightStayWithoutInvoice cannot be null")
    @Min(value = 0, message = "overnightStayWithoutInvoice cannot be negative")
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;

    @NotNull(message = "Number of overnight stay with invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay with invoice cannot be negative")
    private Integer inputQuantityOfOvernightStayWithInvoice = 0;

    @NotNull(message = "overnightStayWithInvoice cannot be null")
    @Min(value = 0, message = "overnightStayWithInvoice cannot be negative")
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;

    public OvernightStayDto(Integer inputQuantityOfOvernightStayWithoutInvoice, BigDecimal amountOfTotalOvernightsStayWithoutInvoice,
                            Integer inputQuantityOfOvernightStayWithInvoice, BigDecimal amountOfTotalOvernightsStayWithInvoice) {
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.amountOfTotalOvernightsStayWithoutInvoice = amountOfTotalOvernightsStayWithoutInvoice;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
    }
}
