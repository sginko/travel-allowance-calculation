package pl.sginko.travelexpense.domen.travelexpense.travel.service;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.domen.auth.entity.UserEntity;
import pl.sginko.travelexpense.domen.auth.service.userService.UserReaderService;
import pl.sginko.travelexpense.domen.auth.util.AuthenticationUtil;
import pl.sginko.travelexpense.domen.emailService.EmailService;
import pl.sginko.travelexpense.domen.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.domen.travelexpense.diet.service.DietService;
import pl.sginko.travelexpense.domen.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domen.travelexpense.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.domen.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.domen.travelexpense.transportCost.service.TransportCostService;
import pl.sginko.travelexpense.domen.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.domen.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.domen.travelexpense.travel.dto.TravelSubmissionResponseDto;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelStatus;
import pl.sginko.travelexpense.domen.travelexpense.travel.event.TravelSubmissionEvent;
import pl.sginko.travelexpense.domen.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.domen.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.domen.travelexpense.travel.repository.TravelRepository;

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
//    private final EmailService emailService;

    @Override
    @Transactional
    public TravelSubmissionResponseDto calculateTravelExpenses(final TravelRequestDto travelRequestDto) {
        String email = AuthenticationUtil.getCurrentUserEmail();
        UserEntity currentUser = userReaderService.findUserByEmail(email);

        TravelEntity travelEntity = travelMapper.toTravelEntity(travelRequestDto, currentUser);

        travelEntity.changeStatus(TravelStatus.SUBMITTED);

        DietEntity dietEntity = dietService.createDietEntity(travelRequestDto.getDietDto(), travelEntity);
        travelEntity.updateDietEntity(dietEntity);

        OvernightStayEntity overnightStayEntity = overnightStayService.createOvernightStayEntity(travelRequestDto.getOvernightStayDto(), travelEntity);
        travelEntity.updateOvernightStayEntity(overnightStayEntity);

        TransportCostEntity transportCostEntity = transportCostService.createTransportCostEntity(travelRequestDto.getTransportCostDto(), travelEntity);
        travelEntity.updateTransportCostEntity(transportCostEntity);

        travelEntity.updateTotalAmount();

        travelRepository.save(travelEntity);

//        emailService.sendSubmissionNotification(currentUser.getEmail(), travelEntity.getTechId());

        publishSubmintEvent(travelEntity, currentUser);

        return travelMapper.toTravelSubmissionResponseDto(travelEntity);
    }

    private void publishSubmintEvent(TravelEntity travelEntity, UserEntity currentUser) {
        TravelSubmissionEvent submissionEvent = new TravelSubmissionEvent(
                travelEntity.getTechId(),
                currentUser.getEmail()
        );
        eventPublisher.publishEvent(submissionEvent);
    }

    @Override
    public List<TravelResponseDto> getAllTravelsByUser() {
        String email = AuthenticationUtil.getCurrentUserEmail();
        List<TravelEntity> allByUserEntityEmail = travelRepository.findAllByUserEntity_Email(email);
        return allByUserEntityEmail.stream()
                .map(entity -> travelMapper.toResponseDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public TravelSubmissionResponseDto getTravelByTechId(UUID techId) {
        TravelEntity travelEntity = travelRepository.findByTechId(techId)
                .orElseThrow(() -> new TravelException("Travel not found"));
        return travelMapper.toTravelSubmissionResponseDto(travelEntity);
    }

    @Scheduled(cron = "0 54 21 * * *")
    @Transactional
    public void cleanupOldReports() {
        LocalDate cutoffDate = LocalDate.now().minusDays(1);
        List<TravelEntity> oldReports = travelRepository.findByStartDateBefore(cutoffDate);

        oldReports.forEach(entity -> travelRepository.delete(entity));
        System.out.println("Old reports was deleted.");
    }


}
