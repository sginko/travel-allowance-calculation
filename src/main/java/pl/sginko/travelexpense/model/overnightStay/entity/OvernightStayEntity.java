package pl.sginko.travelexpense.model.overnightStay.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sginko.travelexpense.model.travel.TravelException;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "overnight_stay")
public class OvernightStayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithoutInvoice;

    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithoutInvoice = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer inputQuantityOfOvernightStayWithInvoice;

    @Column(nullable = false)
    private BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal overnightStayAmount = BigDecimal.ZERO;

    public OvernightStayEntity(TravelEntity travelEntity, Integer inputQuantityOfOvernightStayWithoutInvoice,
                               Integer inputQuantityOfOvernightStayWithInvoice, BigDecimal amountOfTotalOvernightsStayWithInvoice) {
        this.travelEntity = travelEntity;
        this.inputQuantityOfOvernightStayWithoutInvoice = inputQuantityOfOvernightStayWithoutInvoice;
        this.inputQuantityOfOvernightStayWithInvoice = inputQuantityOfOvernightStayWithInvoice;
        this.amountOfTotalOvernightsStayWithInvoice = amountOfTotalOvernightsStayWithInvoice;
        calculateOvernightStayAmount();
    }

    public void calculateOvernightStayAmount() {
        int quantityOfOvernightStay = getTotalQuantityOfNight();
        BigDecimal dailyAllowance = BigDecimal.valueOf(45);
        BigDecimal oneNightWithInvoice = dailyAllowance.multiply(BigDecimal.valueOf(20));
        BigDecimal oneNightWithoutInvoice = dailyAllowance.multiply(BigDecimal.valueOf(1.5));

        if (inputQuantityOfOvernightStayWithoutInvoice > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        } else {
            amountOfTotalOvernightsStayWithoutInvoice = oneNightWithoutInvoice.multiply(BigDecimal.valueOf(inputQuantityOfOvernightStayWithoutInvoice));
        }

        if (inputQuantityOfOvernightStayWithInvoice > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        }

        if ((inputQuantityOfOvernightStayWithInvoice + inputQuantityOfOvernightStayWithoutInvoice) > quantityOfOvernightStay) {
            throw new TravelException("Total input numbers of overnight stay more than total overnight stay");
        }

        this.overnightStayAmount = amountOfTotalOvernightsStayWithoutInvoice.add(amountOfTotalOvernightsStayWithInvoice);
    }

    private int getTotalQuantityOfNight() {
        int night = 0;
        LocalDate startDate = travelEntity.getStartDate();
        LocalTime startTime = travelEntity.getStartTime();
        LocalDate endDate = travelEntity.getEndDate();
        LocalTime endTime = travelEntity.getEndTime();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        while (startDateTime.isBefore(endDateTime)) {
            LocalDateTime endOfCurrentNight = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);

            if (endDateTime.isBefore(endOfCurrentNight)) {
                endOfCurrentNight = endDateTime;
            }

            LocalDateTime startOfCurrentNight = startDateTime.withHour(21).withMinute(0).withSecond(0);

            if (startDateTime.isAfter(startOfCurrentNight)) {
                startOfCurrentNight = startDateTime;
            }

            if (Duration.between(startOfCurrentNight, endOfCurrentNight).toHours() >= 6) {
                night++;
            }

            startDateTime = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
        }
        return night;
    }
}
