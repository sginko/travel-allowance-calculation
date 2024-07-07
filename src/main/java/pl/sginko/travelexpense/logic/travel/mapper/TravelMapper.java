package pl.sginko.travelexpense.logic.travel.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.user.service.UserReaderService;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

@Component
public class TravelMapper {
    private final UserReaderService userReaderService;

    public TravelMapper(UserReaderService userReaderService) {
        this.userReaderService = userReaderService;
    }

    public TravelResponseDto toResponseDto(TravelEntity entity) {
        return new TravelResponseDto(entity.getId(), entity.getFromCity(), entity.getToCity(), entity.getStartDate(),
                entity.getStartTime(), entity.getEndDate(), entity.getEndTime(), entity.getNumberOfBreakfasts(),
                entity.getNumberOfLunches(), entity.getNumberOfDinners(), entity.getTotalAmount(),
                entity.getDietAmount(), entity.getFoodAmount(), entity.getUserEntity().getPesel(),
                entity.getQuantityOfOvernightStay(), entity.getInputQuantityOfOvernightStayWithoutInvoice(),
                entity.getAmountOfTotalOvernightsStayWithoutInvoice(), entity.getInputQuantityOfOvernightStayWithInvoice(),
                entity.getAmountOfTotalOvernightsStayWithInvoice(), entity.getAdvancePayment());
    }

    public TravelEntity toEntity(TravelRequestDto requestDto) {
        return new TravelEntity(requestDto.getDailyAllowance(), requestDto.getFromCity(), requestDto.getToCity(), requestDto.getStartDate(),
                requestDto.getStartTime(), requestDto.getEndDate(), requestDto.getEndTime(),
                requestDto.getNumberOfBreakfasts(), requestDto.getNumberOfLunches(), requestDto.getNumberOfDinners(),
                userReaderService.findEmployeeByPesel(requestDto.getPesel()), requestDto.getInputQuantityOfOvernightStayWithoutInvoice(),
                requestDto.getInputQuantityOfOvernightStayWithInvoice(), requestDto.getAmountOfTotalOvernightsStayWithInvoice(),
                requestDto.getAdvancePayment());
    }
}
