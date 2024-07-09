package pl.sginko.travelexpense.logic.travel.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.diet.service.DietService;
import pl.sginko.travelexpense.logic.employee.model.entity.EmployeeEntity;
import pl.sginko.travelexpense.logic.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.logic.overnightStay.model.dto.CalculatedOvernightStay;
import pl.sginko.travelexpense.logic.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.logic.overnightStay.service.OvernightStayServiceImpl;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travel.repository.TravelRepository;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelMapper travelMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;
    private final EmployeeReaderService employeeReaderService;

    @Override
    @Transactional
    public TravelResponseDto calculateTravelExpenses(final TravelRequestDto travelRequestDto) {
        BigDecimal dietAmount = dietService.calculateDiet(travelRequestDto);
        CalculatedOvernightStay overnightStayResponseDto = overnightStayService.calculateOvernightStay(travelRequestDto);
        int totalQuantityOfNight = ((OvernightStayServiceImpl) overnightStayService).getTotalQuantityOfNight(travelRequestDto);
        TravelEntity travelEntity = travelMapper.toEntity(travelRequestDto);
        EmployeeEntity employeeByPesel = employeeReaderService.findEmployeeByPesel(travelRequestDto.getPesel());

        travelEntity.getOvernightStayEntity().setQuantityOfOvernightStay(totalQuantityOfNight);
        travelEntity.getDietEntity().setDietAmount(dietAmount);
        travelEntity.getOvernightStayEntity().setOvernightStayAmount(overnightStayResponseDto.getOvernightStayAmount());
        travelEntity.updateTotalAmount();
//        TravelEntity travelEntity = travelMapper.toEntity(TravelMapper.Source.of(
//                travelRequestDto,
//                employeeByPesel
//        ));

        travelRepository.save(travelEntity);
        return travelMapper.toResponseDto(travelEntity);
    }
}
