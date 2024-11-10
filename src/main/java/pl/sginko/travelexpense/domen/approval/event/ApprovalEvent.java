package pl.sginko.travelexpense.domen.approval.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ApprovalEvent {
    private final UUID travelTechId;
    private final String userEmail;
    private final TravelStatus status;
}
