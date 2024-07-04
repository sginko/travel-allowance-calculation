package pl.sginko.travelexpense.model.travel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.diet.service.DietService;
import pl.sginko.travelexpense.model.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.model.travel.repository.TravelRepository;

@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;

    public TravelServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper,
                             DietService dietService, OvernightStayService overnightStayService) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
        this.dietService = dietService;
        this.overnightStayService = overnightStayService;
    }

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto travelRequestDto) {
        TravelEntity travelEntity = travelMapper.toEntity(travelRequestDto);

        dietService.calculateDiet(travelRequestDto, dietEntity);

        overnightStayService.calculateOvernightStay(travelRequestDto, overnightStayEntity);

        travelEntity.updateTotalAmount();

        travelRepository.save(travelEntity);
        return travelMapper.toResponseDto(travelEntity);
    }
}
