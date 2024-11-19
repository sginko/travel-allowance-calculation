package pl.sginko.travelexpense.domain.travelReport.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportRequestDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;

@AllArgsConstructor
@Component
public class TravelReportMapper {
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;
    private final TransportCostMapper transportCostMapper;

    public TravelReportEntity toTravelEntity(TravelReportRequestDto travelReportRequestDto, UserEntity userEntity) {
        return new TravelReportEntity(
                travelReportRequestDto.getFromCity(),
                travelReportRequestDto.getToCity(),
                travelReportRequestDto.getStartDate(),
                travelReportRequestDto.getStartTime(),
                travelReportRequestDto.getEndDate(),
                travelReportRequestDto.getEndTime(),
                userEntity,
                travelReportRequestDto.getAdvancePayment(),
                travelReportRequestDto.getOtherExpenses());
    }

    public TravelReportResponseDto toResponseDto(TravelReportEntity entity) {
        return new TravelReportResponseDto(
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

    public TravelReportSubmissionResponseDto toTravelSubmissionResponseDto(TravelReportEntity entity) {
        return new TravelReportSubmissionResponseDto(entity.getTechId(), entity.getStatus());
    }

    public TravelReportEditDto toTravelEditDto(TravelReportEntity entity) {
        return new TravelReportEditDto(
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