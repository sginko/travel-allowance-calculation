/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.domain.travelReport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.domain.travelReport.dto.transportCost.TransportCostEditDto;
import pl.sginko.travelexpense.domain.travelReport.exception.TransportException;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "transport_cost")
public class TransportCostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "transportCostEntity")
    private TravelReportEntity travelReportEntity;

    @NotNull(message = "Days For Undocumented Transport Cost cannot be null")
    @Column(nullable = false)
    private Integer daysForUndocumentedLocalTransportCost;

    @Column(nullable = false)
    private BigDecimal undocumentedLocalTransportCost;

    @Column(nullable = false)
    private BigDecimal documentedLocalTransportCost;

    @NotBlank(message = "Means Of Transport cannot be blank")
    @Column(nullable = false)
    private String meansOfTransport;

    @Column(nullable = false)
    private BigDecimal costOfTravelByPublicTransport;

    @NotNull(message = "Kilometers by car engine up to 900cc cannot be null")
    @Min(value = 0, message = "Kilometers by car engine up to 900cc cannot be negative")
    @Column(nullable = false)
    private Long kilometersByCarEngineUpTo900cc;

    @NotNull(message = "Kilometers by car engine above 900cc cannot be null")
    @Min(value = 0, message = "Kilometers by car engine above 900cc cannot be negative")
    @Column(nullable = false)
    private Long kilometersByCarEngineAbove900cc;

    @NotNull(message = "Kilometers by motorcycle cannot be null")
    @Min(value = 0, message = "Kilometers by motorcycle cannot be negative")
    @Column(nullable = false)
    private Long kilometersByMotorcycle;

    @NotNull(message = "Kilometers by moped cannot be null")
    @Min(value = 0, message = "Kilometers by moped cannot be negative")
    @Column(nullable = false)
    private Long kilometersByMoped;

    @Column(nullable = false)
    private BigDecimal costOfTravelByOwnTransport;

    @Column(nullable = false)
    private BigDecimal totalCostOfTravelByOwnAndPublicTransport;

    @Column(nullable = false)
    private BigDecimal transportCostAmount;

    private static final BigDecimal COST_BY_CAR_ENGINE_UP_TO_900_CC = BigDecimal.valueOf(0.89);
    private static final BigDecimal COST_BY_CAR_ENGINE_ABOVE_900_CC = BigDecimal.valueOf(1.15);
    private static final BigDecimal COST_BY_MOTORCYCLE = BigDecimal.valueOf(0.69);
    private static final BigDecimal COST_BY_MOPED = BigDecimal.valueOf(0.42);

    public TransportCostEntity(TravelReportEntity travelReportEntity, Integer daysForUndocumentedLocalTransportCost,
                               BigDecimal documentedLocalTransportCost, String meansOfTransport,
                               BigDecimal costOfTravelByPublicTransport, Long kilometersByCarEngineUpTo900cc,
                               Long kilometersByCarEngineAbove900cc, Long kilometersByMotorcycle, Long kilometersByMoped) {
        this.travelReportEntity = travelReportEntity;
        this.daysForUndocumentedLocalTransportCost = daysForUndocumentedLocalTransportCost != null ? daysForUndocumentedLocalTransportCost : 0;
        this.documentedLocalTransportCost = documentedLocalTransportCost != null ? documentedLocalTransportCost : BigDecimal.ZERO;
        this.meansOfTransport = meansOfTransport != null ? meansOfTransport : "";
        this.costOfTravelByPublicTransport = costOfTravelByPublicTransport != null ? costOfTravelByPublicTransport : BigDecimal.ZERO;
        this.kilometersByCarEngineUpTo900cc = kilometersByCarEngineUpTo900cc != null ? kilometersByCarEngineUpTo900cc : 0;
        this.kilometersByCarEngineAbove900cc = kilometersByCarEngineAbove900cc != null ? kilometersByCarEngineAbove900cc : 0;
        this.kilometersByMotorcycle = kilometersByMotorcycle != null ? kilometersByMotorcycle : 0;
        this.kilometersByMoped = kilometersByMoped != null ? kilometersByMoped : 0;

        this.costOfTravelByOwnTransport = calculateCostOfTravelByOwnTransport();
        this.undocumentedLocalTransportCost = calculateUndocumentedLocalTransportCost();
        this.transportCostAmount = calculateTransportCostAmount();
        this.totalCostOfTravelByOwnAndPublicTransport = calculateTotalCostOfTravelByOwnAndPublicTransport();
    }

    public BigDecimal calculateTransportCostAmount() {
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (documentedLocalTransportCost.compareTo(BigDecimal.ZERO) > 0) {
            totalAmount = documentedLocalTransportCost;
        } else {
            totalAmount = undocumentedLocalTransportCost;
        }

        return totalAmount.add(costOfTravelByPublicTransport).add(costOfTravelByOwnTransport);
    }

    public void updateTransportCostDetails(TransportCostEditDto transportCostEditDto) {
        this.daysForUndocumentedLocalTransportCost = transportCostEditDto.getDaysForUndocumentedLocalTransportCost();
        this.documentedLocalTransportCost = transportCostEditDto.getDocumentedLocalTransportCost();
        this.meansOfTransport = transportCostEditDto.getMeansOfTransport();
        this.costOfTravelByPublicTransport = transportCostEditDto.getCostOfTravelByPublicTransport();
        this.kilometersByCarEngineUpTo900cc = transportCostEditDto.getKilometersByCarEngineUpTo900cc();
        this.kilometersByCarEngineAbove900cc = transportCostEditDto.getKilometersByCarEngineAbove900cc();
        this.kilometersByMotorcycle = transportCostEditDto.getKilometersByMotorcycle();
        this.kilometersByMoped = transportCostEditDto.getKilometersByMoped();

        this.costOfTravelByOwnTransport = calculateCostOfTravelByOwnTransport();
        this.undocumentedLocalTransportCost = calculateUndocumentedLocalTransportCost();
        this.transportCostAmount = calculateTransportCostAmount();
        this.totalCostOfTravelByOwnAndPublicTransport = calculateTotalCostOfTravelByOwnAndPublicTransport();
    }

    private BigDecimal calculateTotalCostOfTravelByOwnAndPublicTransport() {
        return costOfTravelByPublicTransport.add(calculateCostOfTravelByOwnTransport());
    }

    private BigDecimal calculateUndocumentedLocalTransportCost() {
        if (documentedLocalTransportCost.compareTo(BigDecimal.ZERO) > 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal dailyAllowance = travelReportEntity.getDietEntity().getDailyAllowance();
        BigDecimal dailyUndocumentedLocalTransportCost = dailyAllowance.multiply(BigDecimal.valueOf(0.20));
        Long daysInTravel = getDaysInTravel();

        if (daysForUndocumentedLocalTransportCost > daysInTravel) {
            throw new TransportException("The number of days entered for undocumented Local Transport Costs is greater than the number of days on the trip");
        }

        return dailyUndocumentedLocalTransportCost.multiply(BigDecimal.valueOf(daysForUndocumentedLocalTransportCost));
    }

    private BigDecimal calculateCostOfTravelByOwnTransport() {
        BigDecimal amountCostByCarEngineUpTo900Cc = COST_BY_CAR_ENGINE_UP_TO_900_CC.multiply(BigDecimal.valueOf(kilometersByCarEngineUpTo900cc));
        BigDecimal amountCostByCarEngineAboveTo900Cc = COST_BY_CAR_ENGINE_ABOVE_900_CC.multiply(BigDecimal.valueOf(kilometersByCarEngineAbove900cc));
        BigDecimal amountCostByMotorcycle = COST_BY_MOTORCYCLE.multiply(BigDecimal.valueOf(kilometersByMotorcycle));
        BigDecimal amountCostByMoped = COST_BY_MOPED.multiply(BigDecimal.valueOf(kilometersByMoped));

        return amountCostByCarEngineUpTo900Cc.add(amountCostByCarEngineAboveTo900Cc).add(amountCostByMotorcycle).add(amountCostByMoped);
    }

    private Long getDaysInTravel() {
        long hoursInTravel = getDurationInHours();
        long daysInTravel = hoursInTravel / 24;
        long remainingHours = hoursInTravel % 24;

        if (remainingHours > 0) {
            daysInTravel++;
        }
        return daysInTravel;
    }

    private long getDurationInHours() {
        return Duration.between(travelReportEntity.getStartTime().atDate(travelReportEntity.getStartDate()),
                travelReportEntity.getEndTime().atDate(travelReportEntity.getEndDate())).toHours();
    }
}
