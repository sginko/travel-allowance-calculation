package pl.sginko.travelexpense.logic.travelexpense.travel.service;


import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;

public interface TravelService {

    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);
}
