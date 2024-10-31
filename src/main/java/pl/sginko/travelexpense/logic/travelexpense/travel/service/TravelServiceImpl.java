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
import pl.sginko.travelexpense.logic.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;

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

        TransportCostEntity transportCostEntity = transportCostService.createTransportCostEntity(travelEntity, travelRequestDto.getTransportCostDto());
        travelEntity.updateTransportCostEntity(transportCostEntity);

        travelEntity.updateTotalAmount();

        travelRepository.save(travelEntity);

        return travelMapper.toResponseDto(travelEntity);
    }
}
