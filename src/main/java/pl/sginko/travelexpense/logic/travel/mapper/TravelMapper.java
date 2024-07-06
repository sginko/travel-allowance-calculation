package pl.sginko.travelexpense.logic.travel.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.diet.mapper.DietMapper;
import pl.sginko.travelexpense.logic.employee.model.entity.EmployeeEntity;
import pl.sginko.travelexpense.logic.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.logic.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

@AllArgsConstructor
@Component
public class TravelMapper {
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;
    private final EmployeeReaderService employeeReaderService;

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getEmployeeEntity().getPesel(), entity.getFromCity(), entity.getToCity(),
                entity.getStartDate(), entity.getStartTime(), entity.getEndDate(), entity.getEndTime(),
                entity.getTotalAmount(), entity.getAdvancePayment(), dietMapper.toResponseDto(entity.getDietEntity()),
                overnightStayMapper.toResponseDto(entity.getOvernightStayEntity()));
    }

    public TravelEntity toEntity(TravelRequestDto travelRequestDto) {
        EmployeeEntity employeeByPesel = employeeReaderService.findEmployeeByPesel(travelRequestDto.getPesel());
        return new TravelEntity(travelRequestDto.getFromCity(), travelRequestDto.getToCity(), travelRequestDto.getStartDate(),
                travelRequestDto.getStartTime(), travelRequestDto.getEndDate(), travelRequestDto.getEndTime(),
                employeeByPesel, travelRequestDto.getAdvancePayment(),
                travelRequestDto.getDietDto().getDailyAllowance(), travelRequestDto.getDietDto().getNumberOfBreakfasts(),
                travelRequestDto.getDietDto().getNumberOfLunches(), travelRequestDto.getDietDto().getNumberOfDinners(),
                travelRequestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithoutInvoice(),
                travelRequestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithInvoice(),
                travelRequestDto.getOvernightStayDto().getAmountOfTotalOvernightsStayWithInvoice());
    }

//    public TravelEntity toEntity(Source source) {
//        return new TravelEntity(source.travelRequestDto.getFromCity(), source.travelRequestDto.getToCity(), source.travelRequestDto.getStartDate(),
//                source.travelRequestDto.getStartTime(), source.travelRequestDto.getEndDate(), source.travelRequestDto.getEndTime(),
//                source.employeeEntity, source.travelRequestDto.getAdvancePayment(),
//                source.travelRequestDto.getDietDto().getDailyAllowance(), source.travelRequestDto.getDietDto().getNumberOfBreakfasts(),
//                source.travelRequestDto.getDietDto().getNumberOfLunches(), source.travelRequestDto.getDietDto().getNumberOfDinners(),
//                source.travelRequestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithoutInvoice(),
//                source.travelRequestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithInvoice(),
//                source.travelRequestDto.getOvernightStayDto().getAmountOfTotalOvernightsStayWithInvoice());
//    }
//
//    @Value(staticConstructor = "of")
//    public static class Source {
//        TravelRequestDto travelRequestDto;
//        EmployeeEntity employeeEntity;
//    }
}
