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
        TravelEntity travelEntity = travelMapper.toEntity(travelRequestDto);
        BigDecimal totalAmount = calculateTotalAmount(travelRequestDto);
        UserEntity userByPesel = userReaderService.findUserByPesel(travelRequestDto.getPesel());

        DietEntity dietEntity = travelEntity.getDietEntity();
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);

        OvernightStayEntity overnightStayEntity = travelEntity.getOvernightStayEntity();
        Integer quantityOfOvernightStay = overnightStayService.calculateQuantityOfOvernightStay(travelRequestDto);
        Integer totalInputQuantityOfOvernightStay = overnightStayService.calculateTotalInputQuantityOfOvernightStay(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice = overnightStayService.calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);

//        travelEntity.setUserEntity(userByPesel);
//        travelEntity.setTotalAmount(totalAmount);
        travelEntity.updateTotalAmount(totalAmount);
        travelEntity.updateUser(userByPesel);

//        dietEntity.setDietAmount(dietAmount);
//        dietEntity.setFoodAmount(foodAmount);
        dietEntity.updateDietAmount(dietAmount);
        dietEntity.updateFoodAmount(foodAmount);

//        overnightStayEntity.setQuantityOfOvernightStay(quantityOfOvernightStay);
//        overnightStayEntity.setTotalInputQuantityOfOvernightStay(totalInputQuantityOfOvernightStay);
//        overnightStayEntity.setAmountOfTotalOvernightsStayWithoutInvoice(amountOfTotalOvernightsStayWithoutInvoice);
//        overnightStayEntity.setOvernightStayAmount(overnightStayAmount);
        overnightStayEntity.updateQuantityOfOvernightStay(quantityOfOvernightStay);
        overnightStayEntity.updateTotalInputQuantityOfOvernightStay(totalInputQuantityOfOvernightStay);
        overnightStayEntity.updateAmountOfTotalOvernightsStayWithoutInvoice(amountOfTotalOvernightsStayWithoutInvoice);
        overnightStayEntity.updateOvernightStayAmount(overnightStayAmount);

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
