package pl.sginko.travelexpense.model.service;

import pl.sginko.travelexpense.model.Dto.TravelRequestDto;
import pl.sginko.travelexpense.model.Dto.TravelResponseDto;

public interface TravelService {
    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);
}