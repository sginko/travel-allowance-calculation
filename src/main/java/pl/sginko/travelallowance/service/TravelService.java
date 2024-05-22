package pl.sginko.travelallowance.service;

import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface TravelService {
    void addTravelExpenses(TravelRequestDto travelRequestDto);
//    List<TravelResponseDto> findAllTravelExpenses();
    TravelResponseDto findTravelExpensesForTravelRequest(TravelRequestDto travelRequestDto);
//    BigDecimal getTravelExpenses(TravelRequestDto travelRequestDto);
}