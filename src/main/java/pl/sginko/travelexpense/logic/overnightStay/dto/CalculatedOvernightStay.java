package pl.sginko.travelexpense.logic.overnightStay.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CalculatedOvernightStay {

    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice;
    private BigDecimal amountOfTotalOvernightsStayWithInvoice;
    private BigDecimal overnightStayAmount;
}
