package pl.sginko.travelexpense.logic.travelexpense.travelReport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.dto.TransportCostDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelReportRequestDto {
//    @NotNull(message = "Email cannot be null")
//    private String email;

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
