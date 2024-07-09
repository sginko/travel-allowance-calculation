package pl.sginko.travelexpense.logic.overnightStay.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OvernightStayResponseDto {
    private Long id;
    private Integer inputQuantityOfOvernightStayWithoutInvoice;
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;
    private Integer inputQuantityOfOvernightStayWithInvoice;
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;
    private BigDecimal overnightStayAmount;
}
