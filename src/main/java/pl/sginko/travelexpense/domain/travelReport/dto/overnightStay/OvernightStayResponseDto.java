package pl.sginko.travelexpense.domain.travelReport.dto.overnightStay;

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
    private BigDecimal totalAmountOfOvernightsStayWithoutInvoice;
    private Integer inputQuantityOfOvernightStayWithInvoice;
    private BigDecimal totalAmountOfOvernightsStayWithInvoice;
    private BigDecimal overnightStayAmount;
}
