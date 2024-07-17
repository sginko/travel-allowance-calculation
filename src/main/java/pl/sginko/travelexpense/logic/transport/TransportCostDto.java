package pl.sginko.travelexpense.logic.transport;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransportCostDto {

    private Integer inputtedDaysNumberForTransportCost;

    private BigDecimal documentedTransportCostAmount;

    @NotNull(message = "Daily allowance cannot be null")
    private BigDecimal undocumentedTransportCostAmount;
}
