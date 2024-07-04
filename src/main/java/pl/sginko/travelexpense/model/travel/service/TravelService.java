package pl.sginko.travelexpense.model.travel.service;

import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;

public interface TravelService {

    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);
}
