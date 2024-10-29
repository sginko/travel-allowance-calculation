package pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto;

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
    private Integer quantityOfOvernightStay;
    private Integer totalInputQuantityOfOvernightStay;
    private Integer inputQuantityOfOvernightStayWithoutInvoice;
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;
    private Integer inputQuantityOfOvernightStayWithInvoice;
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;
    private BigDecimal overnightStayAmount;
}