package pl.sginko.travelexpense.logic.travelexpense.travel.service;

import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelSubmissionResponseDto;

import java.util.List;
import java.util.UUID;

public interface TravelService {

    //    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);
    TravelSubmissionResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);

    List<TravelResponseDto> getAllTravelsByUser();

    TravelSubmissionResponseDto getTravelByTechId(UUID techId);
//
//    void deleteAllTravelsByUser();
//
//    void deleteTravelByIdByUser(UUID techId);
}
