package pl.sginko.travelexpense.model.travel.service;

import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.model.travel.repository.TravelRepository;

public class TravelServiceImpl implements TravelService {
    private final TravelMapper travelMapper;
    private final TravelRepository travelRepository;

    public TravelServiceImpl(TravelMapper travelMapper, TravelRepository travelRepository) {
        this.travelMapper = travelMapper;
        this.travelRepository = travelRepository;
    }

    @Override
    public void addTravel(TravelRequestDto requestDto) {
        travelRepository.save(travelMapper.toEntity(requestDto));
    }
}
