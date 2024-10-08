package pl.sginko.travelexpense.logic.travel.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.diet.model.dto.DietDto;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.transport.model.dto.TransportCostDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelRequestDto {
    @NotNull(message = "Pessel cannot be null")
    @Size(min = 11, max = 11, message = "Pesel must be exactly 11 characters")
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
    private BigDecimal advancePayment;

    @NotNull(message = "Other expenses cannot be null")
    private BigDecimal otherExpenses;

    @NotNull(message = "DietDto cannot be null")
    private DietDto dietDto;

    @NotNull(message = "OvernightStayDto cannot be null")
    private OvernightStayDto overnightStayDto;

    @NotNull(message = "TransportCostDto cannot be null")
    private TransportCostDto transportCostDto;
}
