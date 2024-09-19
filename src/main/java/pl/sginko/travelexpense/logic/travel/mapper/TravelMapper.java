package pl.sginko.travelexpense.logic.travel.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.diet.model.dto.DietDto;
import pl.sginko.travelexpense.logic.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.transport.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.transport.model.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;
import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.service.UserReaderService;

@AllArgsConstructor
@Component
public class TravelMapper {
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;
    private final TransportCostMapper transportCostMapper;
    private final UserReaderService userReaderService;

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getUserEntity().getPesel(), entity.getFromCity(), entity.getToCity(),
                entity.getStartDate(), entity.getStartTime(), entity.getEndDate(), entity.getEndTime(), entity.getOtherExpenses(),
                entity.getTotalAmount(), entity.getAdvancePayment(), dietMapper.toResponseDto(entity.getDietEntity()),
                overnightStayMapper.toResponseDto(entity.getOvernightStayEntity()),
                transportCostMapper.toResponseDto(entity.getTransportCostEntity()));
    }

    public TravelEntity toEntity(TravelRequestDto travelRequestDto) {
        UserEntity userByPesel = userReaderService.findUserByPesel(travelRequestDto.getPesel());
        DietDto dietDto = travelRequestDto.getDietDto();
        OvernightStayDto overnightStayDto = travelRequestDto.getOvernightStayDto();
        TransportCostDto transportCostDto = travelRequestDto.getTransportCostDto();
        return new TravelEntity(travelRequestDto.getFromCity(), travelRequestDto.getToCity(), travelRequestDto.getStartDate(),
                travelRequestDto.getStartTime(), travelRequestDto.getEndDate(), travelRequestDto.getEndTime(),
                userByPesel, travelRequestDto.getAdvancePayment(), dietDto.getDailyAllowance(), dietDto.getNumberOfBreakfasts(),
                dietDto.getNumberOfLunches(), dietDto.getNumberOfDinners(), overnightStayDto.getInputQuantityOfOvernightStayWithoutInvoice(),
                overnightStayDto.getInputQuantityOfOvernightStayWithInvoice(), overnightStayDto.getAmountOfTotalOvernightsStayWithInvoice(),
                overnightStayDto.getIsInvoiceAmountGreaterAllowed(), transportCostDto.getInputtedDaysNumberForUndocumentedLocalTransportCost(),
                transportCostDto.getDocumentedLocalTransportCost(), transportCostDto.getMeansOfTransport(), transportCostDto.getCostOfTravelByPublicTransport(),
                transportCostDto.getKilometersByCarEngineUpTo900cc(), transportCostDto.getKilometersByCarEngineAbove900cc(),
                transportCostDto.getKilometersByMotorcycle(), transportCostDto.getKilometersByMoped(), travelRequestDto.getOtherExpenses());
    }
}
