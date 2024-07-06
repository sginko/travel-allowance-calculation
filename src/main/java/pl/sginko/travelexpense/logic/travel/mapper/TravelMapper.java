package pl.sginko.travelexpense.logic.travel.mapper;

import lombok.Value;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.employee.model.entity.EmployeeEntity;
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
        return new TravelEntity(source.travelRequestDto.getFromCity(), source.travelRequestDto.getToCity(), source.travelRequestDto.getStartDate(),
                source.travelRequestDto.getStartTime(), source.travelRequestDto.getEndDate(), source.travelRequestDto.getEndTime(),
                source.employeeEntity, source.travelRequestDto.getAdvancePayment(),
                source.travelRequestDto.getDietDto().getDailyAllowance(), source.travelRequestDto.getDietDto().getNumberOfBreakfasts(),
                source.travelRequestDto.getDietDto().getNumberOfLunches(), source.travelRequestDto.getDietDto().getNumberOfDinners(),
                source.travelRequestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithoutInvoice(),
                source.travelRequestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithInvoice(),
                source.travelRequestDto.getOvernightStayDto().getAmountOfTotalOvernightsStayWithInvoice());
    }

    @Value(staticConstructor = "of")
    public static class Source {
        TravelRequestDto travelRequestDto;
        EmployeeEntity employeeEntity;
    }
}
