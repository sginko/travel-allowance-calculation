package pl.sginko.travelexpense.domain.travelReport.service;

import com.github.fge.jsonpatch.JsonPatch;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportRequestDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportSubmissionResponseDto;

import java.util.List;
import java.util.UUID;

public interface TravelReportService {

    TravelReportSubmissionResponseDto createTravelExpenseReport(TravelReportRequestDto travelReportRequestDto);

    List<TravelReportResponseDto> getUserTravelExpenseReports();

    TravelReportSubmissionResponseDto getTravelExpenseReportById(UUID techId);

    void cleanupOldReports();

    void updateTravelExpenseReportById(UUID techId, JsonPatch patch);
}
