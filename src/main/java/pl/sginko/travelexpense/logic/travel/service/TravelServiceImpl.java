package pl.sginko.travelexpense.logic.travel.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.diet.model.entity.DietEntity;
import pl.sginko.travelexpense.logic.diet.service.DietService;
import pl.sginko.travelexpense.logic.overnightStay.model.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.logic.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travel.repository.TravelRepository;
import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.service.UserReaderService;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;
    private final UserReaderService userReaderService;

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(final TravelRequestDto travelRequestDto) {
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice = overnightStayService.calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);
        //BigDecimal amountOfTotalOvernightsStayWithInvoice = overnightStayService.calculateAmountOfOvernightStayWithInvoice(travelRequestDto);
        BigDecimal totalAmount = calculateTotalAmount(travelRequestDto);
        UserEntity employeeByPesel = userReaderService.findUserByPesel(travelRequestDto.getPesel());

        TravelEntity travelEntity = travelMapper.toEntity(travelRequestDto);
        travelEntity.setUserEntity(employeeByPesel);
        travelEntity.setTotalAmount(totalAmount);

        DietEntity dietEntity = travelEntity.getDietEntity();
        dietEntity.setDietAmount(dietAmount);
        dietEntity.setFoodAmount(dietService.calculateFoodAmount(travelRequestDto));

        OvernightStayEntity overnightStayEntity = travelEntity.getOvernightStayEntity();
        overnightStayEntity.setQuantityOfOvernightStay(overnightStayService.calculateQuantityOfOvernightStay(travelRequestDto));
        overnightStayEntity.setTotalInputQuantityOfOvernightStay(overnightStayService.calculateTotalInputQuantityOfOvernightStay(travelRequestDto));
        overnightStayEntity.setAmountOfTotalOvernightsStayWithoutInvoice(amountOfTotalOvernightsStayWithoutInvoice);
        //overnightStayEntity.setAmountOfTotalOvernightsStayWithInvoice(amountOfTotalOvernightsStayWithInvoice);
        overnightStayEntity.setOvernightStayAmount(overnightStayAmount);

        travelRepository.save(travelEntity);
        return travelMapper.toResponseDto(travelEntity);
    }

    private BigDecimal calculateTotalAmount(TravelRequestDto travelRequestDto) {
        BigDecimal advancePayment = travelRequestDto.getAdvancePayment();
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        return dietAmount.add(overnightStayAmount).subtract(advancePayment);
    }
}
