package pl.sginko.travelexpense.logic.travelexpense.travel.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.service.userService.UserReaderService;
import pl.sginko.travelexpense.logic.travelexpense.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.travelexpense.diet.dto.DietDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.transport.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.transport.dto.TransportCostDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
//import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;
//import pl.sginko.travelexpense.logic.user.service.UserReaderService;

@AllArgsConstructor
@Component
public class TravelMapper {
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;
    private final TransportCostMapper transportCostMapper;
    private final UserReaderService userReaderService;

//    public TravelResponseDto toResponseDto(TravelEntity entity) {
//        return new TravelResponseDto(entity.getId(), entity.getUserEntity().getEmail(), entity.getFromCity(), entity.getToCity(),
//                entity.getStartDate(), entity.getStartTime(), entity.getEndDate(), entity.getEndTime(), entity.getOtherExpenses(),
//                entity.getTotalAmount(), entity.getAdvancePayment(), dietMapper.toResponseDto(entity.getDietEntity()),
//                overnightStayMapper.toResponseDto(entity.getOvernightStayEntity()),
//                transportCostMapper.toResponseDto(entity.getTransportCostEntity()));
//    }

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getUserEntity().getEmail(), entity.getFromCity(), entity.getToCity(),
                entity.getStartDate(), entity.getStartTime(), entity.getEndDate(), entity.getEndTime(), entity.getOtherExpenses(),
                entity.getTotalAmount(), entity.getAdvancePayment(), dietMapper.toResponseDto(entity.getDietEntity()),
                overnightStayMapper.toResponseDto(entity.getOvernightStayEntity()),
                transportCostMapper.toResponseDto(entity.getTransportCostEntity()));
    }

    public TravelEntity toEntity(TravelRequestDto travelRequestDto) {
        UserEntity userByPesel = userReaderService.findUserByEmail(travelRequestDto.getEmail());
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
