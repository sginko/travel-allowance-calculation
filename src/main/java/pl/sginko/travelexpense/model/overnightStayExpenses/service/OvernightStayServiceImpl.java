package pl.sginko.travelexpense.model.overnightStayExpenses.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.dietExpenses.service.DietExpensesService;
import pl.sginko.travelexpense.model.overnightStayExpenses.OvernightStayException;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.model.travel.service.TravelService;

import java.math.BigDecimal;

@Service
public class OvernightStayServiceImpl implements OvernightStayService {
    private final TravelMapper travelMapper;
//    private final TravelService travelService;
    private final DietExpensesService dietExpensesService;

    public OvernightStayServiceImpl(TravelMapper travelMapper, @Lazy TravelService travelService, @Lazy DietExpensesService dietExpensesService) {
        this.travelMapper = travelMapper;
//        this.travelService = travelService;
        this.dietExpensesService = dietExpensesService;
    }

    @Override
    public BigDecimal calculateOvernightStayAmount(TravelRequestDto travelRequestDto) {
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice;

//        Integer quantityOfOvernightStay = travelService.getTotalQuantityOfNight(travelRequestDto);

        BigDecimal oneNightWithInvoice = dietExpensesService.getDAILY_ALLOWANCE().multiply(BigDecimal.valueOf(20));
        BigDecimal oneNightWithoutInvoice = dietExpensesService.getDAILY_ALLOWANCE().multiply(BigDecimal.valueOf(1.5));

//        if (inputQuantityOfOvernightStayWithoutInvoice > quantityOfOvernightStay) {
//            throw new OvernightStayException("Input quantity overnight stay more than quantity overnight stay");
//        } else {
            amountOfTotalOvernightsStayWithoutInvoice = oneNightWithoutInvoice.multiply(BigDecimal.valueOf(inputQuantityOfOvernightStayWithoutInvoice));
//        }

//        if (inputQuantityOfOvernightStayWithInvoice > quantityOfOvernightStay) {
//            throw new OvernightStayException("Input quantity overnight stay more than quantity overnight stay");
//        }
//
//        if ((inputQuantityOfOvernightStayWithInvoice + inputQuantityOfOvernightStayWithoutInvoice) > quantityOfOvernightStay) {
//            throw new OvernightStayException("Total input numbers of overnight stay more than total overnight stay");
//        }
        Integer totalInputQuantityOfOvernightStay = inputQuantityOfOvernightStayWithInvoice + inputQuantityOfOvernightStayWithoutInvoice;
        BigDecimal overnightStayAmount = amountOfTotalOvernightsStayWithoutInvoice.add(amountOfTotalOvernightsStayWithInvoice);
        return overnightStayAmount;

//       travelService.updateTotalAmount()
    }

    @Override
    public BigDecimal amountOfTotalOvernightsStayWithInvoice(TravelRequestDto requestDto){
        return requestDto.getAmountOfTotalOvernightsStayWithInvoice();
    }
}
