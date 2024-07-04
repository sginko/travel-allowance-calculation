package pl.sginko.travelexpense.model.travel.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.diet.mapper.DietMapper;
import pl.sginko.travelexpense.model.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.model.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Component
public class TravelMapper {
    private final EmployeeReaderService employeeReaderService;
    private final DietMapper dietMapper;
    private final OvernightStayMapper overnightStayMapper;

    public TravelMapper(EmployeeReaderService employeeReaderService, DietMapper dietMapper, OvernightStayMapper overnightStayMapper) {
        this.employeeReaderService = employeeReaderService;
        this.dietMapper = dietMapper;
        this.overnightStayMapper = overnightStayMapper;
    }

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getEmployeeEntity().getPesel(), entity.getFromCity(), entity.getToCity(),
                entity.getStartDate(), entity.getStartTime(), entity.getEndDate(), entity.getEndTime(),
                entity.getTotalAmount(), entity.getAdvancePayment(), dietMapper.toResponseDto(entity.getDietEntity()),
                overnightStayMapper.toResponseDto(entity.getOvernightStayEntity()));
    }

    public TravelEntity toEntity(TravelRequestDto requestDto) {
        return new TravelEntity(requestDto.getFromCity(), requestDto.getToCity(), requestDto.getStartDate(),
                requestDto.getStartTime(), requestDto.getEndDate(), requestDto.getEndTime(),
                employeeReaderService.findEmployeeByPesel(requestDto.getPesel()), requestDto.getAdvancePayment(),
                requestDto.getDietDto().getDailyAllowance(), requestDto.getDietDto().getNumberOfBreakfasts(),
                requestDto.getDietDto().getNumberOfLunches(), requestDto.getDietDto().getNumberOfDinners(),
                requestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithoutInvoice(),
                requestDto.getOvernightStayDto().getInputQuantityOfOvernightStayWithInvoice(),
                requestDto.getOvernightStayDto().getAmountOfTotalOvernightsStayWithInvoice());
    }
}
