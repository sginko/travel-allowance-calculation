package pl.sginko.travelexpense.model.travel.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Component
public class TravelMapper {
    private final EmployeeReaderService employeeReaderService;

    public TravelMapper(EmployeeReaderService employeeReaderService) {
        this.employeeReaderService = employeeReaderService;
    }

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getCityFrom(), entity.getCityTo(), entity.getStartDate(),
                entity.getStartTime(), entity.getEndDate(), entity.getEndTime(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners(), entity.getTotalAmount(),
                entity.getDietAmount(), entity.getFoodAmount(), entity.getEmployeeEntity().getPesel());
    }

    public TravelEntity toEntity(TravelRequestDto requestDto) {
        return new TravelEntity(requestDto.getCityFrom(), requestDto.getCityTo(), requestDto.getStartDate(),
                requestDto.getStartTime(), requestDto.getEndDate(), requestDto.getEndTime(),
                requestDto.getDAILY_ALLOWANCE(), requestDto.getNumberOfBreakfasts(),
                requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners(),
                employeeReaderService.findEmployeeByPesel(requestDto.getPesel()));
    }
}
