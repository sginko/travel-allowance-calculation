package pl.sginko.travelexpense.logic.travelexpense.travel.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.service.userService.UserReaderService;
import pl.sginko.travelexpense.logic.auth.util.AuthenticationUtil;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.diet.service.DietService;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.transport.service.TransportCostService;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;

import java.util.List;
import java.util.Optional;
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

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(final TravelRequestDto travelRequestDto) {
        String email = AuthenticationUtil.getCurrentUserEmail();
        UserEntity currentUser = userReaderService.findUserByEmail(email);

        TravelEntity travelEntity = travelMapper.toTravelEntity(travelRequestDto, currentUser);

        DietEntity dietEntity = dietService.createDietEntity(travelRequestDto.getDietDto(), travelEntity);
        travelEntity.updateDietEntity(dietEntity);

        OvernightStayEntity overnightStayEntity = overnightStayService.createOvernightStayEntity(travelRequestDto.getOvernightStayDto(), travelEntity);
        travelEntity.updateOvernightStayEntity(overnightStayEntity);

        TransportCostEntity transportCostEntity = transportCostService.createTransportCostEntity(travelRequestDto.getTransportCostDto(), travelEntity);
        travelEntity.updateTransportCostEntity(transportCostEntity);

        travelEntity.updateTotalAmount();

        travelRepository.save(travelEntity);

        return travelMapper.toResponseDto(travelEntity);
    }

    public List<TravelResponseDto> getAllTravelsByUser() {
        String email = AuthenticationUtil.getCurrentUserEmail();
        List<TravelEntity> allByUserEntityEmail = travelRepository.findAllByUserEntity_Email(email);
        return allByUserEntityEmail.stream()
                .map(entity -> travelMapper.toResponseDto(entity))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteAllTravelsByUser() {
        String email = AuthenticationUtil.getCurrentUserEmail();
        travelRepository.deleteAllByUserEntity_Email(email);
    }

    @Transactional
    @Override
    public void deleteTravelByIdByUser(UUID techId) {
        String email = AuthenticationUtil.getCurrentUserEmail();
        Optional<TravelEntity> optionalTravelEntity = travelRepository.findByTechIdAndUserEntity_Email(techId, email);

        if (optionalTravelEntity.isPresent()) {
            travelRepository.delete(optionalTravelEntity.get());
        } else {
            throw new TravelException("Travel with techId " + techId + " not found for user " + email);
        }
    }
}
