package pl.sginko.travelexpense.logic.inboxMessage;

import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;

public interface InboxService {

    void saveIncomingMessage(TravelRequestDto requestDto);

    void processInboxMessages();
}
