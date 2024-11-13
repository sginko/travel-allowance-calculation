package pl.sginko.travelexpense.logic.travelexpense.travel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietEditDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayEditDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostEditDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelEditDto {
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
    private DietEditDto dietEditDtoDto;

    @NotNull(message = "OvernightStayDto cannot be null")
    private OvernightStayEditDto overnightStayEditDto;

    @NotNull(message = "TransportCostDto cannot be null")
    private TransportCostEditDto transportCostEditDto;
}
