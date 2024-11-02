package pl.sginko.travelexpense.logic.travelexpense.travel.service;

import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;

import java.util.List;

public interface TravelService {

    List<TravelResponseDto> getAllTravelsByUser();

    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);
}
