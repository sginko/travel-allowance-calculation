package pl.sginko.travelexpense.logic.travelexpense.travelReport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TravelReportSubmissionResponseDto {
    private UUID techId;
    private TravelReportStatus status;
}
