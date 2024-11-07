//package pl.sginko.travelexpense.logic.inboxMessage;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import org.jobrunr.scheduling.JobScheduler;
//import org.springframework.stereotype.Service;
//import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
//import pl.sginko.travelexpense.logic.travelexpense.travel.service.TravelService;
//
//import java.util.List;
//
//@AllArgsConstructor
//@Service
//public class InboxServiceImpl implements InboxService {
//    private final InboxMessageRepository inboxMessageRepository;
//    private final ObjectMapper objectMapper;
//    private final JobScheduler jobScheduler;
//    private final TravelService travelService;
//
//    @Override
//    public void saveIncomingMessage(TravelRequestDto requestDto) {
//        try {
//            String payload = objectMapper.writeValueAsString(requestDto);
//            InboxMessage inboxMessage = new InboxMessage(payload);
//            inboxMessageRepository.save(inboxMessage);
//
//            jobScheduler.enqueue(() -> processInboxMessages());
//        } catch (JsonProcessingException e) {
//            throw new InboxException("Error inbox");
//        }
//    }
//
//    @Override
//    public void processInboxMessages() {
//        List<InboxMessage> messages = inboxMessageRepository.findByProcessedFalse();
//        for (InboxMessage message : messages) {
//            try {
//                TravelRequestDto requestDto = objectMapper.readValue(message.getPayload(), TravelRequestDto.class);
//                travelService.calculateTravelExpenses(requestDto);
//                message.markAsProcessed();
//                inboxMessageRepository.save(message);
//            } catch (Exception e) {
//                throw new InboxException("Error inbox");
//            }
//        }
//    }
//}
