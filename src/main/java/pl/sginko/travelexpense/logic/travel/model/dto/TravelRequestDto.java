package pl.sginko.travelexpense.logic.travel.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.overnightStay.dto.OvernightStayDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelRequestDto {

    @NotNull(message = "PESEL cannot be null")
    private Long pesel;

    @NotBlank(message = "From city cannot be blank")
    @Size(min = 2, max = 50, message = "City name should be between 2 and 50 characters")
    private String fromCity;

    @NotBlank(message = "To city cannot be blank")
    @Size(min = 2, max = 50, message = "City name should be between 2 и 50 characters")
    private String toCity;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

    @NotNull(message = "Advance payment cannot be null")
    @Size(min = 0, message = "Advance payment cannot be negative")
    private BigDecimal advancePayment;

    @NotNull
    private DietDto dietDto;

    @NotNull
    private OvernightStayDto overnightStayDto;

    public TravelRequestDto(Long pesel, String fromCity, String toCity, LocalDate startDate, LocalTime startTime,
                            LocalDate endDate, LocalTime endTime, BigDecimal advancePayment, DietDto dietDto,
                            OvernightStayDto overnightStayDto) {
        this.pesel = pesel;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.advancePayment = advancePayment;
        this.dietDto = dietDto;
        this.overnightStayDto = overnightStayDto;
    }
}
