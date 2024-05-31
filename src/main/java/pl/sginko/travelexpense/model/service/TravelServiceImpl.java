package pl.sginko.travelexpense.model.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.Dto.TravelRequestDto;
import pl.sginko.travelexpense.model.Dto.TravelResponseDto;
import pl.sginko.travelexpense.model.Entity.TravelEntity;
import pl.sginko.travelexpense.model.Mapper.TravelMapper;
import pl.sginko.travelexpense.model.repository.TravelRepository;

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