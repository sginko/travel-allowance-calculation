package pl.sginko.travelexpense.model.travel.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.dietExpenses.entity.DietExpensesEntity;
import pl.sginko.travelexpense.model.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.model.overnightStayExpenses.entity.OvernightStayExpensesEntity;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Component
public class TravelMapper {
    private final EmployeeReaderService employeeReaderService;

    public TravelMapper(EmployeeReaderService employeeReaderService) {
        this.employeeReaderService = employeeReaderService;
    }

    public TravelResponseDto toResponseDto(TravelEntity travelEntity, DietExpensesEntity dietExpensesEntity,
                                           OvernightStayExpensesEntity overnightStayExpensesEntity) {
        return new TravelResponseDto(travelEntity.getId(), travelEntity.getFromCity(), travelEntity.getToCity(), travelEntity.getStartDate(),
                travelEntity.getStartTime(), travelEntity.getEndDate(), travelEntity.getEndTime(), dietExpensesEntity.getNumberOfBreakfasts(),
                dietExpensesEntity.getNumberOfLunches(), dietExpensesEntity.getNumberOfDinners(), travelEntity.getTotalAmount(),
                dietExpensesEntity.getDietAmount(), dietExpensesEntity.getFoodAmount(), travelEntity.getEmployeeEntity().getPesel(),
                overnightStayExpensesEntity.getQuantityOfOvernightStay(), overnightStayExpensesEntity.getInputQuantityOfOvernightStayWithoutInvoice(),
                overnightStayExpensesEntity.getAmountOfTotalOvernightsStayWithoutInvoice(), overnightStayExpensesEntity.getInputQuantityOfOvernightStayWithInvoice(),
                overnightStayExpensesEntity.getAmountOfTotalOvernightsStayWithInvoice(), travelEntity.getAdvancePayment());
    }

    public TravelEntity toEntityFromTravelService(TravelRequestDto requestDto) {
        return new TravelEntity(employeeReaderService.findEmployeeByPesel(requestDto.getPesel()), requestDto.getFromCity(),
                requestDto.getToCity(), requestDto.getStartDate(), requestDto.getStartTime(), requestDto.getEndDate(),
                requestDto.getEndTime(), requestDto.getAdvancePayment());
    }

    public DietExpensesEntity toEntityFromDietService(TravelRequestDto requestDto) {
        return new DietExpensesEntity(requestDto.getNumberOfBreakfasts(), requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners());
    }

    public OvernightStayExpensesEntity toEntityFromOvernightStayService(TravelRequestDto requestDto) {
        return new OvernightStayExpensesEntity(requestDto.getInputQuantityOfOvernightStayWithoutInvoice(), requestDto.getInputQuantityOfOvernightStayWithInvoice(),
                requestDto.getAmountOfTotalOvernightsStayWithInvoice());
    }
}
