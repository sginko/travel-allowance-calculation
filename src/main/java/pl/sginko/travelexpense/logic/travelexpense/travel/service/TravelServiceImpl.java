package pl.sginko.travelexpense.logic.travelexpense.travel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.diet.service.DietService;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.service.TransportCostService;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelEditDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;
import pl.sginko.travelexpense.logic.travelexpense.travel.event.TravelSubmissionEvent;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.service.userService.UserReaderService;
import pl.sginko.travelexpense.logic.user.util.AuthenticationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;
    private final TransportCostService transportCostService;
    private final UserReaderService userReaderService;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
//    private final EmailService emailService;

    @Override
    @Transactional
    public TravelSubmissionResponseDto createTravelExpenseReport(final TravelRequestDto travelRequestDto) {
        String email = AuthenticationUtil.getCurrentUserEmail();
        UserEntity currentUser = userReaderService.findUserByEmail(email);

        TravelEntity travelEntity = travelMapper.toTravelEntity(travelRequestDto, currentUser);

        travelEntity.updateStatus(TravelStatus.SUBMITTED);

        DietEntity dietEntity = dietService.createDietEntity(travelRequestDto.getDietDto(), travelEntity);
        travelEntity.setDietDetails(dietEntity);

        OvernightStayEntity overnightStayEntity = overnightStayService.createOvernightStayEntity(travelRequestDto.getOvernightStayDto(), travelEntity);
        travelEntity.setOvernightStayDetails(overnightStayEntity);

        TransportCostEntity transportCostEntity = transportCostService.createTransportCostEntity(travelRequestDto.getTransportCostDto(), travelEntity);
        travelEntity.setTransportCostDetails(transportCostEntity);

        travelEntity.calculateTotalAmount();

        travelRepository.save(travelEntity);

//        emailService.sendSubmissionNotification(currentUser.getEmail(), travelEntity.getTechId());

        publishSubmitEvent(travelEntity, currentUser);

        return travelMapper.toTravelSubmissionResponseDto(travelEntity);
    }

    @Override
    public List<TravelResponseDto> getUserTravelExpenseReports() {
        String email = AuthenticationUtil.getCurrentUserEmail();
        List<TravelEntity> allByUserEntityEmail = travelRepository.findAllByUserEntity_Email(email);
        return allByUserEntityEmail.stream()
                .map(entity -> travelMapper.toResponseDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public TravelSubmissionResponseDto getTravelExpenseReportById(UUID techId) {
        TravelEntity travelEntity = getTravelEntity(techId);

        return travelMapper.toTravelSubmissionResponseDto(travelEntity);
    }

    @Override
    @Transactional
    public void updateTravelExpenseReportById(UUID techId, JsonPatch patch) {
        TravelEntity travelEntity = getTravelEntity(techId);
        TravelEditDto travelEditDto = travelMapper.toTravelEditDto(travelEntity);

        try {
            JsonNode jsonNode = objectMapper.convertValue(travelEditDto, JsonNode.class);
            JsonNode patched = patch.apply(jsonNode);
            TravelEditDto travelEdited = objectMapper.treeToValue(patched, TravelEditDto.class);

            travelEntity.updateTravelDetails(travelEdited);

        } catch (JsonPatchException | JsonProcessingException e) {
            throw new TravelException("Error update travel ", e);
        }
    }

    @Scheduled(cron = "0 54 21 * * *")
    @Transactional
    public void cleanupOldReports() {
        LocalDate cutoffDate = LocalDate.now().minusDays(1);
        List<TravelEntity> oldReports = travelRepository.findByStartDateBefore(cutoffDate);

        oldReports.forEach(entity -> travelRepository.delete(entity));
        System.out.println("Old reports was deleted.");
    }

    private TravelEntity getTravelEntity(UUID techId) {
        TravelEntity travelEntity = travelRepository.findByTechId(techId)
                .orElseThrow(() -> new TravelException("Travel not found"));
        return travelEntity;
    }

    private void publishSubmitEvent(TravelEntity travelEntity, UserEntity currentUser) {
        TravelSubmissionEvent submissionEvent = new TravelSubmissionEvent(
                travelEntity.getTechId(),
                currentUser.getEmail()
        );
        eventPublisher.publishEvent(submissionEvent);
    }
}
