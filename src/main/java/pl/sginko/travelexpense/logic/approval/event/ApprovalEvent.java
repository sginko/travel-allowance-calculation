package pl.sginko.travelexpense.logic.approval.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ApprovalEvent {
    private final UUID travelTechId;
    private final String userEmail;
    private final TravelStatus status;
}
