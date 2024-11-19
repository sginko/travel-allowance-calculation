package pl.sginko.travelexpense.domain.travelReport.dto.travelReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TravelReportSubmissionResponseDto {
    private UUID techId;
    private TravelReportStatus status;
}
