package pl.sginko.travelexpense.logic.travelexpense.travel.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.travelexpense.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.mapper.TransportCostMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelEditDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;

@AllArgsConstructor
@Component
public class TravelMapper {
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;
    private final TransportCostMapper transportCostMapper;

    public TravelEntity toTravelEntity(TravelRequestDto travelRequestDto, UserEntity userEntity) {
        return new TravelEntity(
                travelRequestDto.getFromCity(),
                travelRequestDto.getToCity(),
                travelRequestDto.getStartDate(),
                travelRequestDto.getStartTime(),
                travelRequestDto.getEndDate(),
                travelRequestDto.getEndTime(),
                userEntity,
                travelRequestDto.getAdvancePayment(),
                travelRequestDto.getOtherExpenses());
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
                transportCostMapper.toResponseDto(entity.getTransportCostEntity()));
    }

    public TravelSubmissionResponseDto toTravelSubmissionResponseDto(TravelEntity entity) {
        return new TravelSubmissionResponseDto(entity.getTechId(), entity.getStatus());
    }

    public TravelEditDto toTravelEditDto(TravelEntity entity) {
        return new TravelEditDto(
                entity.getFromCity(),
                entity.getToCity(),
                entity.getStartDate(),
                entity.getStartTime(),
                entity.getEndDate(),
                entity.getEndTime(),
                entity.getAdvancePayment(),
                entity.getOtherExpenses(),
                dietMapper.toDietEditDto(entity.getDietEntity()),
                overnightStayMapper.toOvernightStayEditDto(entity.getOvernightStayEntity()),
                transportCostMapper.toTransportCosEditDto(entity.getTransportCostEntity()));
    }
}
