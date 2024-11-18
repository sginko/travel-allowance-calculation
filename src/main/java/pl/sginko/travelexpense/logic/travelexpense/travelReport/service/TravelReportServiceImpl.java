package pl.sginko.travelexpense.logic.travelexpense.travelReport.service;

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
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportEditDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportStatus;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.event.TravelReportSubmissionEvent;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.mapper.TravelReportMapper;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.service.userService.UserReaderService;
import pl.sginko.travelexpense.logic.user.util.AuthenticationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TravelReportServiceImpl implements TravelReportService {
    private final TravelReportRepository travelReportRepository;
    private final TravelReportMapper travelReportMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;
    private final TransportCostService transportCostService;
    private final UserReaderService userReaderService;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
//    private final EmailService emailService;

    @Override
    @Transactional
    public TravelReportSubmissionResponseDto createTravelExpenseReport(final TravelReportRequestDto travelReportRequestDto) {
        String email = AuthenticationUtil.getCurrentUserEmail();
        UserEntity currentUser = userReaderService.findUserByEmail(email);

        TravelReportEntity travelReportEntity = travelReportMapper.toTravelEntity(travelReportRequestDto, currentUser);

        travelReportEntity.updateStatus(TravelReportStatus.SUBMITTED);

        DietEntity dietEntity = dietService.createDietEntity(travelReportRequestDto.getDietDto(), travelReportEntity);
        travelReportEntity.setDietDetails(dietEntity);

        OvernightStayEntity overnightStayEntity = overnightStayService.createOvernightStayEntity(travelReportRequestDto.getOvernightStayDto(), travelReportEntity);
        travelReportEntity.setOvernightStayDetails(overnightStayEntity);

        TransportCostEntity transportCostEntity = transportCostService.createTransportCostEntity(travelReportRequestDto.getTransportCostDto(), travelReportEntity);
        travelReportEntity.setTransportCostDetails(transportCostEntity);

        travelReportEntity.calculateTotalAmount();

        travelReportRepository.save(travelReportEntity);

//        emailService.sendSubmissionNotification(currentUser.getEmail(), travelEntity.getTechId());

        publishSubmitEvent(travelReportEntity, currentUser);

        return travelReportMapper.toTravelSubmissionResponseDto(travelReportEntity);
    }

    @Override
    public List<TravelReportResponseDto> getUserTravelExpenseReports() {
        String email = AuthenticationUtil.getCurrentUserEmail();

        List<TravelReportEntity> allByUserEntityEmail = travelReportRepository.findAllByUserEntity_Email(email);

        return allByUserEntityEmail.stream()
                .map(entity -> travelReportMapper.toResponseDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public TravelReportSubmissionResponseDto getTravelExpenseReportById(UUID techId) {
        TravelReportEntity travelReportEntity = getTravelEntity(techId);

        return travelReportMapper.toTravelSubmissionResponseDto(travelReportEntity);
    }

    @Override
    @Transactional
    public void updateTravelExpenseReportById(UUID techId, JsonPatch patch) {
        TravelReportEntity travelReportEntity = getTravelEntity(techId);
        TravelReportEditDto travelReportEditDto = travelReportMapper.toTravelEditDto(travelReportEntity);

        try {
            JsonNode jsonNode = objectMapper.convertValue(travelReportEditDto, JsonNode.class);
            JsonNode patched = patch.apply(jsonNode);
            TravelReportEditDto travelEdited = objectMapper.treeToValue(patched, TravelReportEditDto.class);

            travelReportEntity.updateTravelDetails(travelEdited);

        } catch (JsonPatchException | JsonProcessingException e) {
            throw new TravelReportException("Error update travel ", e);
        }
    }

    @Scheduled(cron = "${cleanup.old-reports.cron}")
    @Transactional
    public void cleanupOldReports() {
        LocalDate cutoffDate = LocalDate.now().minusDays(1);

        List<TravelReportEntity> oldReports = travelReportRepository.findByStartDateBefore(cutoffDate);

        oldReports.forEach(entity -> travelReportRepository.delete(entity));

        System.out.println("Old reports was deleted.");
    }

    private TravelReportEntity getTravelEntity(UUID techId) {
        String email = AuthenticationUtil.getCurrentUserEmail();

        return travelReportRepository.findByTechIdAndUserEntity_Email(techId, email)
                .orElseThrow(() -> new TravelReportException("Travel not found"));
    }

    private void publishSubmitEvent(TravelReportEntity travelReportEntity, UserEntity currentUser) {
        TravelReportSubmissionEvent submissionEvent = new TravelReportSubmissionEvent(travelReportEntity.getTechId(),
                currentUser.getEmail());

        eventPublisher.publishEvent(submissionEvent);
    }
}
