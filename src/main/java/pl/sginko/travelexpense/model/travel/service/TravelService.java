package pl.sginko.travelexpense.model.travel.service;

import pl.sginko.travelexpense.model.diet.dto.DietRequestDto;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;

import java.math.BigDecimal;

public interface TravelService {

    TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto);
//    TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto, DietRequestDto dietRequestDto, OvernightStayRequestDto overnightStayRequestDto);

//    Integer getTotalQuantityOfNight(TravelRequestDto requestDto);
//
//    BigDecimal updateTotalAmount(TravelRequestDto requestDto);
//
//    TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto);
}
