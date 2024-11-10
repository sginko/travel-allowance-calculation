package pl.sginko.travelexpense.domen.travelexpense.travel.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.sginko.travelexpense.domen.auth.entity.UserEntity;
import pl.sginko.travelexpense.domen.auth.service.userService.UserReaderService;
import pl.sginko.travelexpense.domen.auth.util.AuthenticationUtil;
import pl.sginko.travelexpense.domen.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.domen.travelexpense.diet.service.DietService;
import pl.sginko.travelexpense.domen.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domen.travelexpense.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.domen.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.domen.travelexpense.transportCost.service.TransportCostService;
import pl.sginko.travelexpense.domen.travelexpense.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.domen.travelexpense.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.domen.travelexpense.travel.repository.TravelRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TravelServiceTest {
    @Test
    void testCalculateTravelExpenses() {
        // GIVEN: Set up the necessary mocks and dependencies
        TravelRepository travelRepository = mock(TravelRepository.class);
        TravelMapper travelMapper = mock(TravelMapper.class);
        DietService dietService = mock(DietService.class);
        OvernightStayService overnightStayService = mock(OvernightStayService.class);
        TransportCostService transportCostService = mock(TransportCostService.class);
        UserReaderService userReaderService = mock(UserReaderService.class);

        TravelServiceImpl travelService = new TravelServiceImpl(
                travelRepository, travelMapper, dietService, overnightStayService, transportCostService, userReaderService);

        TravelRequestDto travelRequestDto = mock(TravelRequestDto.class);
        TravelEntity travelEntity = mock(TravelEntity.class);
        UserEntity userEntity = mock(UserEntity.class);

        // Mock static method
        Mockito.mockStatic(AuthenticationUtil.class);
        when(AuthenticationUtil.getCurrentUserEmail()).thenReturn("test@example.com");
        when(userReaderService.findUserByEmail("test@example.com")).thenReturn(userEntity);
        when(travelMapper.toTravelEntity(travelRequestDto, userEntity)).thenReturn(travelEntity);

        DietEntity dietEntity = mock(DietEntity.class);
        OvernightStayEntity overnightStayEntity = mock(OvernightStayEntity.class);
        TransportCostEntity transportCostEntity = mock(TransportCostEntity.class);

        when(dietService.createDietEntity(any(), eq(travelEntity))).thenReturn(dietEntity);
        when(overnightStayService.createOvernightStayEntity(any(), eq(travelEntity))).thenReturn(overnightStayEntity);
        when(transportCostService.createTransportCostEntity(any(), eq(travelEntity))).thenReturn(transportCostEntity);

        // WHEN
        travelService.calculateTravelExpenses(travelRequestDto);

        // THEN
        verify(travelRepository).save(travelEntity);
    }
}