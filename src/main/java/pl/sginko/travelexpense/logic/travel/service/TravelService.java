package pl.sginko.travelexpense.logic.travel.service;

import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;

public interface TravelService {
    TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto);
}
