package pl.sginko.travelexpense.model.travel.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.diet.dto.DietRequestDto;
import pl.sginko.travelexpense.model.diet.mapper.DietMapper;
import pl.sginko.travelexpense.model.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayRequestDto;
import pl.sginko.travelexpense.model.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Component
public class TravelMapper {
//    private final EmployeeReaderService employeeReaderService;
//
//    public TravelMapper(EmployeeReaderService employeeReaderService) {
//        this.employeeReaderService = employeeReaderService;
//    }

//    public TravelResponseDto toResponseDto(TravelEntity travelEntity, DietEntity dietEntity,
//                                           OvernightStayEntity overnightStayEntity) {
//        return new TravelResponseDto(travelEntity.getId(), travelEntity.getFromCity(), travelEntity.getToCity(), travelEntity.getStartDate(),
//                travelEntity.getStartTime(), travelEntity.getEndDate(), travelEntity.getEndTime(), dietEntity.getNumberOfBreakfasts(),
//                dietEntity.getNumberOfLunches(), dietEntity.getNumberOfDinners(), travelEntity.getTotalAmount(),
//                dietEntity.getDietAmount(), dietEntity.getFoodAmount(), travelEntity.getEmployeeEntity().getPesel(),
//                overnightStayEntity.getQuantityOfOvernightStay(), overnightStayEntity.getInputQuantityOfOvernightStayWithoutInvoice(),
//                overnightStayEntity.getAmountOfTotalOvernightsStayWithoutInvoice(), overnightStayEntity.getInputQuantityOfOvernightStayWithInvoice(),
//                overnightStayEntity.getAmountOfTotalOvernightsStayWithInvoice(), travelEntity.getAdvancePayment());
//    }
//
//    public TravelEntity toEntityFromTravelService(TravelRequestDto requestDto) {
//        return new TravelEntity(employeeReaderService.findEmployeeByPesel(requestDto.getPesel()), requestDto.getFromCity(),
//                requestDto.getToCity(), requestDto.getStartDate(), requestDto.getStartTime(), requestDto.getEndDate(),
//                requestDto.getEndTime(), requestDto.getAdvancePayment());
//    }
//
//    public DietEntity toEntityFromDietService(TravelRequestDto requestDto) {
//        return new DietEntity(requestDto.getNumberOfBreakfasts(), requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners());
//    }
//
//    public OvernightStayEntity toEntityFromOvernightStayService(TravelRequestDto requestDto) {
//        return new OvernightStayEntity(requestDto.getInputQuantityOfOvernightStayWithoutInvoice(), requestDto.getInputQuantityOfOvernightStayWithInvoice(),
//                requestDto.getAmountOfTotalOvernightsStayWithInvoice());
//    }


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

//    public TravelEntity toEntity(TravelRequestDto requestDto) {
//        return new TravelEntity(requestDto.getFromCity(), requestDto.getToCity(), requestDto.getStartDate(),
//                requestDto.getStartTime(), requestDto.getEndDate(), requestDto.getEndTime(),
//                employeeReaderService.findEmployeeByPesel(requestDto.getPesel()), requestDto.getAdvancePayment(),
//                requestDto.getDietRequest().getNumberOfBreakfasts(), requestDto.getDietRequest().getNumberOfLunches(),
//                requestDto.getDietRequest().getNumberOfDinners(), requestDto.getOvernightStayRequestDto().getInputQuantityOfOvernightStayWithoutInvoice(),
//                requestDto.getOvernightStayRequestDto().getInputQuantityOfOvernightStayWithInvoice(),
//                requestDto.getOvernightStayRequestDto().getAmountOfTotalOvernightsStayWithInvoice());
//    }

    public TravelEntity toEntity(TravelRequestDto requestDto) {
        return new TravelEntity(requestDto.getFromCity(), requestDto.getToCity(), requestDto.getStartDate(),
                requestDto.getStartTime(), requestDto.getEndDate(), requestDto.getEndTime(),
                employeeReaderService.findEmployeeByPesel(requestDto.getPesel()), requestDto.getAdvancePayment(),
                requestDto.getDietRequest().getNumberOfBreakfasts(), requestDto.getDietRequest().getNumberOfLunches(),
                requestDto.getDietRequest().getNumberOfDinners(), requestDto.getOvernightStayRequestDto().getInputQuantityOfOvernightStayWithoutInvoice(),
                requestDto.getOvernightStayRequestDto().getInputQuantityOfOvernightStayWithInvoice(),
                requestDto.getOvernightStayRequestDto().getAmountOfTotalOvernightsStayWithInvoice());
    }

//    public TravelEntity toEntity(TravelRequestDto travelRequestDto, DietRequestDto dietRequestDto, OvernightStayRequestDto overnightStayRequestDto) {
//        return new TravelEntity(travelRequestDto.getFromCity(), travelRequestDto.getToCity(), travelRequestDto.getStartDate(),
//                travelRequestDto.getStartTime(), travelRequestDto.getEndDate(), travelRequestDto.getEndTime(),
//                employeeReaderService.findEmployeeByPesel(travelRequestDto.getPesel()), travelRequestDto.getAdvancePayment(),
//                dietRequestDto.getNumberOfBreakfasts(), dietRequestDto.getNumberOfLunches(),
//                dietRequestDto.getNumberOfDinners(), overnightStayRequestDto.getInputQuantityOfOvernightStayWithoutInvoice(),
//                overnightStayRequestDto.getInputQuantityOfOvernightStayWithInvoice(),
//                overnightStayRequestDto.getAmountOfTotalOvernightsStayWithInvoice());
//    }
}
