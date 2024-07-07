package pl.sginko.travelexpense.logic.travel.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

@Component
public class TravelMapper {
    private final EmployeeReaderService employeeReaderService;

    public TravelMapper(EmployeeReaderService employeeReaderService) {
        this.employeeReaderService = employeeReaderService;
    }

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getFromCity(), entity.getToCity(), entity.getStartDate(),
                entity.getStartTime(), entity.getEndDate(), entity.getEndTime(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners(), entity.getTotalAmount(),
                entity.getDietAmount(), entity.getFoodAmount(), entity.getEmployeeEntity().getPesel(),
                entity.getQuantityOfOvernightStay(), entity.getInputQuantityOfOvernightStayWithoutInvoice(),
                entity.getAmountOfTotalOvernightsStayWithoutInvoice(), entity.getInputQuantityOfOvernightStayWithInvoice(),
                entity.getAmountOfTotalOvernightsStayWithInvoice(), entity.getAdvancePayment());
    }

    public TravelEntity toEntity(TravelRequestDto requestDto) {
        return new TravelEntity(requestDto.getFromCity(), requestDto.getToCity(), requestDto.getStartDate(),
                requestDto.getStartTime(), requestDto.getEndDate(), requestDto.getEndTime(),
                requestDto.getNumberOfBreakfasts(), requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners(),
                employeeReaderService.findEmployeeByPesel(requestDto.getPesel()), requestDto.getInputQuantityOfOvernightStayWithoutInvoice(),
                requestDto.getInputQuantityOfOvernightStayWithInvoice(), requestDto.getAmountOfTotalOvernightsStayWithInvoice(),
                requestDto.getAdvancePayment());
    }
}
