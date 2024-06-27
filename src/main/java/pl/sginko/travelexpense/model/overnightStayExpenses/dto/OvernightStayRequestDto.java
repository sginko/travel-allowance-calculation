package pl.sginko.travelexpense.model.overnightStayExpenses.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OvernightStayRequestDto {
    @NotNull(message = "Number of overnight stay without invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay without invoice cannot be negative")
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @NotNull(message = "overnightStayWithoutInvoice cannot be null")
    @Min(value = 0, message = "overnightStayWithoutInvoice cannot be negative")
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;

    @NotNull(message = "Number of overnight stay with invoice cannot be null")
    @Min(value = 0, message = "Number of overnight stay with invoice cannot be negative")
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @NotNull(message = "overnightStayWithoutInvoice cannot be null")
    @Min(value = 0, message = "overnightStayWithoutInvoice cannot be negative")
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;

    public OvernightStayRequestDto(Integer inputQuantityOfOvernightStayWithoutInvoice,
                                   Integer inputQuantityOfOvernightStayWithInvoice,
                                   BigDecimal amountOfTotalOvernightsStayWithInvoice) {
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
    }
}
