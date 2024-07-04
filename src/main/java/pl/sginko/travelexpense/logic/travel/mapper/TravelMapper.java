package pl.sginko.travelexpense.logic.travel.mapper;

import lombok.Value;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.employee.entity.EmployeeEntity;
import pl.sginko.travelexpense.logic.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.logic.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

@Component
public class TravelMapper {
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;

    public TravelMapper(EmployeeReaderService employeeReaderService, DietMapper dietMapper, OvernightStayMapper overnightStayMapper) {
        this.dietMapper = dietMapper;
        this.overnightStayMapper = overnightStayMapper;
    }

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getEmployeeEntity().getPesel(), entity.getFromCity(), entity.getToCity(),
                entity.getStartDate(), entity.getStartTime(), entity.getEndDate(), entity.getEndTime(),
                entity.getTotalAmount(), entity.getAdvancePayment(), dietMapper.toResponseDto(entity.getDietEntity()),
                overnightStayMapper.toResponseDto(entity.getOvernightStayEntity()));
    }

    public TravelEntity toEntity(Source source) {
        return new TravelEntity(source.requestDto.getFromCity(), source.requestDto.getToCity(), source.requestDto.getStartDate(),
                source.requestDto.getStartTime(), source.requestDto.getEndDate(), source.requestDto.getEndTime(),
                source.employeeEntity, source.requestDto.getAdvancePayment(),
                source.requestDto.getDietDto().getDailyAllowance(), source.requestDto.getDietDto().getNumberOfBreakfasts(),
                source.requestDto.getDietDto().getNumberOfLunches(), source.requestDto.getDietDto().getNumberOfDinners(),
                source.requestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithoutInvoice(),
                source.requestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithInvoice(),
                source.requestDto.getOvernightStayDto().getAmountOfTotalOvernightsStayWithInvoice());
    }
    
    @Value(staticConstructor = "of")
    public static class Source{
        TravelRequestDto requestDto;
        EmployeeEntity employeeEntity;
    }
}
