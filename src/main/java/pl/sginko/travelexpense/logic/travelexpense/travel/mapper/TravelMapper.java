package pl.sginko.travelexpense.logic.travelexpense.travel.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.service.userService.UserReaderService;
import pl.sginko.travelexpense.logic.travelexpense.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travelexpense.transport.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;


@AllArgsConstructor
@Component
public class TravelMapper {
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;
    private final TransportCostMapper transportCostMapper;
//    private final UserReaderService userReaderService;

    public TravelEntity toTravelEntity(TravelRequestDto travelRequestDto, UserEntity userEntity) {
//        UserEntity userByEmail = userReaderService.findUserByEmail(travelRequestDto.getEmail());
        return new TravelEntity(
                travelRequestDto.getFromCity(),
                travelRequestDto.getToCity(),
                travelRequestDto.getStartDate(),
                travelRequestDto.getStartTime(),
                travelRequestDto.getEndDate(),
                travelRequestDto.getEndTime(),
                userEntity,
                travelRequestDto.getAdvancePayment(),
                travelRequestDto.getOtherExpenses()
        );
    }

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(
                entity.getTechId(),
                entity.getUserEntity().getEmail(),
                entity.getFromCity(),
                entity.getToCity(),
                entity.getStartDate(),
                entity.getStartTime(),
                entity.getEndDate(),
                entity.getEndTime(),
                entity.getOtherExpenses(),
                entity.getTotalAmount(),
                entity.getAdvancePayment(),
                dietMapper.toResponseDto(entity.getDietEntity()),
                overnightStayMapper.toResponseDto(entity.getOvernightStayEntity()),
                transportCostMapper.toResponseDto(entity.getTransportCostEntity())
        );
    }
}
