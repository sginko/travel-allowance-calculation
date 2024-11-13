package pl.sginko.travelexpense.logic.travelexpense.travel.service;

import com.github.fge.jsonpatch.JsonPatch;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelSubmissionResponseDto;

import java.util.List;
import java.util.UUID;

public interface TravelService {

    //    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);

    TravelSubmissionResponseDto createTravelExpenseReport(TravelRequestDto travelRequestDto);

    List<TravelResponseDto> getUserTravelExpenseReports();

    TravelSubmissionResponseDto getTravelExpenseReportById(UUID techId);

    void cleanupOldReports();

    void updateTravelExpenseReportById(UUID techId, JsonPatch patch);
}
