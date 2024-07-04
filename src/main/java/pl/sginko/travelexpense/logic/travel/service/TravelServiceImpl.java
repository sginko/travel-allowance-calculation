package pl.sginko.travelexpense.logic.travel.service;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.diet.service.DietService;
import pl.sginko.travelexpense.logic.employee.entity.EmployeeEntity;
import pl.sginko.travelexpense.logic.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.logic.overnightStay.dto.CalculatedOvernightStay;
import pl.sginko.travelexpense.logic.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.logic.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travel.repository.TravelRepository;

import java.math.BigDecimal;

@AllArgsConstructor
class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;
    private final EmployeeReaderService employeeReaderService;

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(final TravelRequestDto travelRequestDto) {
        //TODO calculate diet(invoking diet service)
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);

        //TODO calculate overnightstay(invoking overnightstay service)
        CalculatedOvernightStay overnightStayResponseDto = overnightStayService.calculateOvernightStay(travelRequestDto);

        EmployeeEntity employeeByPesel = employeeReaderService.findEmployeeByPesel(travelRequestDto.getPesel());

        TravelEntity travelEntity = travelMapper.toEntity(TravelMapper.Source.of(
                travelRequestDto,
                employeeByPesel
        ));

        travelRepository.save(travelEntity);
        return travelMapper.toResponseDto(travelEntity);
    }

}
