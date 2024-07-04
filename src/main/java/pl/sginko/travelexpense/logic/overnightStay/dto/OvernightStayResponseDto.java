package pl.sginko.travelexpense.logic.overnightStay.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OvernightStayResponseDto {
    private Long id;
    private Integer inputQuantityOfOvernightStayWithoutInvoice;
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;
    private Integer inputQuantityOfOvernightStayWithInvoice;
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;
    private BigDecimal overnightStayAmount;

    public OvernightStayResponseDto(Long id, Integer inputQuantityOfOvernightStayWithoutInvoice, BigDecimal amountOfTotalOvernightsStayWithoutInvoice,
                                    Integer inputQuantityOfOvernightStayWithInvoice, BigDecimal amountOfTotalOvernightsStayWithInvoice,
                                    BigDecimal overnightStayAmount) {
        this.id = id;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.amountOfTotalOvernightsStayWithoutInvoice = amountOfTotalOvernightsStayWithoutInvoice;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
        this.overnightStayAmount = overnightStayAmount;
    }
}
