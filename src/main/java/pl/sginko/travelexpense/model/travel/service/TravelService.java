package pl.sginko.travelexpense.model.travel.service;

import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;

import java.math.BigDecimal;

public interface TravelService {

    TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto);

//    Integer getTotalQuantityOfNight(TravelRequestDto requestDto);
//
//    BigDecimal updateTotalAmount(TravelRequestDto requestDto);
//
//    TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto);
}
