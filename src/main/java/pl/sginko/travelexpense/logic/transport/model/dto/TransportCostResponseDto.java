package pl.sginko.travelexpense.logic.transport.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TransportCostResponseDto {
    private Long id;
    private Integer inputtedDaysNumberForTransportCost;
    private BigDecimal undocumentedLocalTransportCost;
    private BigDecimal documentedLocalTransportCost;
    private String meansOfTransport;
    private BigDecimal costOfTravelByPublicTransport;
    private BigDecimal costOfTravelByOwnTransport;
    private BigDecimal transportCostAmount;
}
