package pl.sginko.travelexpense.logic.travelexpense.travelReport.service;

import com.github.fge.jsonpatch.JsonPatch;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportSubmissionResponseDto;

import java.util.List;
import java.util.UUID;

public interface TravelReportService {

    //    TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto);

    TravelReportSubmissionResponseDto createTravelExpenseReport(TravelReportRequestDto travelReportRequestDto);

    List<TravelReportResponseDto> getUserTravelExpenseReports();

    TravelReportSubmissionResponseDto getTravelExpenseReportById(UUID techId);

    void cleanupOldReports();

    void updateTravelExpenseReportById(UUID techId, JsonPatch patch);
}
