package pl.sginko.travelexpense.model.travel.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.model.travel.repository.TravelRepository;

@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;

    public TravelServiceImpl(TravelRepository travelRepository, TravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
    }

    @Override
    public TravelResponseDto calculateTravelExpenses(TravelRequestDto requestDto) {
        TravelEntity entity = travelMapper.toEntity(requestDto);
        entity.calculateDietAmount();
        entity.calculateFoodAmount();
        travelRepository.save(entity);
        return travelMapper.toResponseDto(entity);
    }
}
