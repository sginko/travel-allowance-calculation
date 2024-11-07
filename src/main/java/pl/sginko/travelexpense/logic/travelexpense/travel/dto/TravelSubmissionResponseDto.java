package pl.sginko.travelexpense.logic.travelexpense.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TravelSubmissionResponseDto {
    private UUID techId;
    private TravelStatus status;
}
