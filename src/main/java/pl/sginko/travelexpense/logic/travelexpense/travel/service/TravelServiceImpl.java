package pl.sginko.travelexpense.logic.travelexpense.travel.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.service.userService.UserReaderService;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.diet.service.DietService;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transport.service.TransportCostService;
import pl.sginko.travelexpense.logic.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;
//import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;
//import pl.sginko.travelexpense.logic.user.service.UserReaderService;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;
    private final TransportCostService transportCostService;
    private final UserReaderService userReaderService;

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(final TravelRequestDto travelRequestDto) {
        TravelEntity travelEntity = travelMapper.toEntity(travelRequestDto);
        BigDecimal totalAmount = calculateTotalAmount(travelRequestDto);
//        UserEntity userByPesel = userReaderService.findUserByPesel(travelRequestDto.getPesel());
        UserEntity userByEmail = userReaderService.findUserByEmail(travelRequestDto.getEmail());

        DietEntity dietEntity = travelEntity.getDietEntity();
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal foodAmount = dietService.calculateFoodAmount(travelRequestDto);

        OvernightStayEntity overnightStayEntity = travelEntity.getOvernightStayEntity();
        Integer quantityOfOvernightStay = overnightStayService.calculateQuantityOfOvernightStay(travelRequestDto);
        Integer totalInputQuantityOfOvernightStay = overnightStayService.calculateTotalInputQuantityOfOvernightStay(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice = overnightStayService.calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);

        TransportCostEntity transportCostEntity = travelEntity.getTransportCostEntity();
        BigDecimal transportCostAmount = transportCostService.calculateTransportCostAmount(travelRequestDto);
        BigDecimal costOfTravelByPublicTransport = transportCostService.calculateCostOfTravelByPublicTransport(travelRequestDto);
        BigDecimal costOfTravelByOwnTransport = transportCostService.calculateCostOfTravelByOwnTransport(travelRequestDto);
        BigDecimal undocumentedLocalTransportCost = transportCostService.calculateUndocumentedLocalTransportCost(travelRequestDto);
        BigDecimal totalCostOfTravelByOwnAndPublicTransport = transportCostService.calculateTotalCostOfTravelByOwnAndPublicTransport(travelRequestDto);

        travelEntity.updateTotalAmount(totalAmount);
        travelEntity.updateUser(userByEmail);

        dietEntity.updateDietAmount(dietAmount);
        dietEntity.updateFoodAmount(foodAmount);

        overnightStayEntity.updateQuantityOfOvernightStay(quantityOfOvernightStay);
        overnightStayEntity.updateTotalInputQuantityOfOvernightStay(totalInputQuantityOfOvernightStay);
        overnightStayEntity.updateAmountOfTotalOvernightsStayWithoutInvoice(amountOfTotalOvernightsStayWithoutInvoice);
        overnightStayEntity.updateOvernightStayAmount(overnightStayAmount);

        transportCostEntity.updateTransportCostAmount(transportCostAmount);
        transportCostEntity.updateCostOfTravelByPublicTransport(costOfTravelByPublicTransport);
        transportCostEntity.updateCostOfTravelByOwnTransport(costOfTravelByOwnTransport);
        transportCostEntity.updateUndocumentedLocalTransportCost(undocumentedLocalTransportCost);
        transportCostEntity.updateTotalCostOfTravelByOwnAndPublicTransport(totalCostOfTravelByOwnAndPublicTransport);

        travelRepository.save(travelEntity);

        return travelMapper.toResponseDto(travelEntity);
    }

    private BigDecimal calculateTotalAmount(final TravelRequestDto travelRequestDto) {
        BigDecimal advancePayment = travelRequestDto.getAdvancePayment();
        BigDecimal otherExpenses = travelRequestDto.getOtherExpenses();
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        BigDecimal overnightStayAmount = overnightStayService.calculateOvernightStay(travelRequestDto);
        BigDecimal transportCostAmount = transportCostService.calculateTransportCostAmount(travelRequestDto);
        return (dietAmount.add(overnightStayAmount).add(transportCostAmount).add(otherExpenses)).subtract(advancePayment);
    }
}
