package pl.sginko.travelexpense.logic.travelexpense.overnightStay.service;

import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;

import java.math.BigDecimal;

public interface OvernightStayService {

    BigDecimal calculateOvernightStay(TravelRequestDto travelRequestDto);

    BigDecimal calculateAmountOfOvernightStayWithoutInvoice(TravelRequestDto travelRequestDto);

    BigDecimal calculateAmountOfOvernightStayWithInvoice(TravelRequestDto travelRequestDto);

    Integer calculateQuantityOfOvernightStay(TravelRequestDto travelRequestDto);

    Integer calculateTotalInputQuantityOfOvernightStay(TravelRequestDto travelRequestDto);
}
