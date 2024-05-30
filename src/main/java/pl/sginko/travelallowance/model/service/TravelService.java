package pl.sginko.travelallowance.model.service;

import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;

public interface TravelService {
    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);
}