package pl.sginko.travelexpense.model.diet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.diet.dto.DietRequestDto;
import pl.sginko.travelexpense.model.diet.dto.DietResponseDto;
import pl.sginko.travelexpense.model.diet.entity.DietEntity;
import pl.sginko.travelexpense.model.diet.mapper.DietMapper;
import pl.sginko.travelexpense.model.diet.repository.DietRepository;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

@Service
public class DietServiceImpl implements DietService {
    private final DietRepository dietRepository;
    private final DietMapper dietMapper;

    public DietServiceImpl(DietRepository dietRepository, DietMapper dietMapper) {
        this.dietRepository = dietRepository;
        this.dietMapper = dietMapper;
    }

    @Override
    @Transactional
    public DietResponseDto calculateDiet(DietRequestDto requestDto, TravelEntity travelEntity) {
        DietEntity entity = dietMapper.toEntity(requestDto, travelEntity);
        dietRepository.save(entity);
        return dietMapper.toResponseDto(entity);
    }
}
