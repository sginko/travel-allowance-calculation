package pl.sginko.travelexpense.logic.travelexpense.travel.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TravelSubmissionEvent {
    private final UUID travelTechId;
    private final String userEmail;
}