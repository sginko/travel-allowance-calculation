package pl.sginko.travelexpense.model.travel.service;

import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;

public interface TravelReadService {

    long getHoursInTravel(TravelRequestDto requestDto);

    int getTotalQuantityOfNight(TravelRequestDto requestDto);

    void updateTotalAmount(TravelRequestDto requestDto);

    TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto);
}
