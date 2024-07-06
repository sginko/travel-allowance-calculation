package pl.sginko.travelexpense.logic.travel.service;

import pl.sginko.travelexpense.logic.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.dto.TravelResponseDto;

public interface TravelService {
    TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto);
}
