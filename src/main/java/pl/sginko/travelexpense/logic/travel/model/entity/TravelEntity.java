package pl.sginko.travelexpense.logic.travel.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sginko.travelexpense.logic.diet.model.entity.DietEntity;
import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;
import pl.sginko.travelexpense.logic.overnightStay.model.entity.OvernightStayEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "travel")
public class TravelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private UserEntity userEntity;

    @NotBlank
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    @Column(nullable = false)
    private String fromCity;

    @NotBlank
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    @Column(nullable = false)
    private String toCity;

    @NotNull(message = "The Date field cannot be empty")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "The Time field cannot be empty")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "The Date field cannot be empty")
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull(message = "The Time field cannot be empty")
    @Column(nullable = false)
    private LocalTime endTime;

    @NotNull(message = "Advance payment cannot be null")
    @Min(value = 0, message = "Advance payment cannot be negative")
    @Column(nullable = false)
    private BigDecimal advancePayment;

    @OneToOne(mappedBy = "travelEntity", cascade = CascadeType.ALL)
    private DietEntity dietEntity;

    @OneToOne(mappedBy = "travelEntity", cascade = CascadeType.ALL)
    private OvernightStayEntity overnightStayEntity;

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public TravelEntity(String fromCity, String toCity, LocalDate startDate, LocalTime startTime,
                        LocalDate endDate, LocalTime endTime, UserEntity userEntity,
                        BigDecimal advancePayment, BigDecimal dailyAllowance, Integer numberOfBreakfasts,
                        Integer numberOfLunches, Integer numberOfDinners, Integer inputQuantityOfOvernightStayWithoutInvoice,
                        Integer inputQuantityOfOvernightStayWithInvoice, BigDecimal amountOfTotalOvernightsStayWithInvoice) {
        this.userEntity = userEntity;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.advancePayment = advancePayment;
        this.dietEntity = new DietEntity(this, dailyAllowance, numberOfBreakfasts, numberOfLunches, numberOfDinners);
        this.overnightStayEntity = new OvernightStayEntity(this, inputQuantityOfOvernightStayWithoutInvoice,
                inputQuantityOfOvernightStayWithInvoice, amountOfTotalOvernightsStayWithInvoice);
//        updateTotalAmount();
    }

    public void updateTotalAmount() {
        this.totalAmount = this.dietEntity.getDietAmount().add(this.overnightStayEntity.getOvernightStayAmount()).subtract(advancePayment);
    }
}
