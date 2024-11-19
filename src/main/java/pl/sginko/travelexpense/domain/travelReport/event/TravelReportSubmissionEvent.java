package pl.sginko.travelexpense.domain.travelReport.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TravelReportSubmissionEvent {
    private final UUID travelTechId;
    private final String userEmail;
}