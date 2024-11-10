package pl.sginko.travelexpense.domen.travelexpense.transportCost.dto;

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
    private Integer daysForTransportCost;
    private BigDecimal undocumentedLocalTransportCost;
    private BigDecimal documentedLocalTransportCost;
    private String meansOfTransport;
    private BigDecimal costOfTravelByPublicTransport;
    private BigDecimal costOfTravelByOwnTransport;
    private BigDecimal transportCostAmount;
}
