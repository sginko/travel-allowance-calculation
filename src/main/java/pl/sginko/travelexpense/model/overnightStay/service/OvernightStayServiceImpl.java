package pl.sginko.travelexpense.model.overnightStay.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayRequestDto;
import pl.sginko.travelexpense.model.overnightStay.dto.OvernightStayResponseDto;
import pl.sginko.travelexpense.model.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.model.overnightStay.mapper.OvernightStayMapper;
import pl.sginko.travelexpense.model.overnightStay.repository.OvernightStayRepository;
import pl.sginko.travelexpense.model.travel.TravelException;
import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;
import pl.sginko.travelexpense.model.travel.mapper.TravelMapper;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class OvernightStayServiceImpl implements OvernightStayService {
    private final OvernightStayRepository overnightStayRepository;
    private final OvernightStayMapper overnightStayMapper;
    private final TravelMapper travelMapper;

    public OvernightStayServiceImpl(OvernightStayRepository overnightStayRepository, OvernightStayMapper overnightStayMapper, TravelMapper travelMapper) {
        this.overnightStayRepository = overnightStayRepository;
        this.overnightStayMapper = overnightStayMapper;
        this.travelMapper = travelMapper;
    }

    @Override
    @Transactional
    public OvernightStayResponseDto calculateOvernightStay(TravelRequestDto travelRequestDto,
                                                           OvernightStayRequestDto overnightStayRequestDto,
                                                           OvernightStayEntity overnightStayEntity) {
        OvernightStayEntity entity = overnightStayMapper.toEntity(overnightStayRequestDto, travelMapper.toEntity(travelRequestDto));
        calculateOvernightStayAmount(travelRequestDto, overnightStayRequestDto, overnightStayEntity);
        overnightStayRepository.save(entity);
        return overnightStayMapper.toResponseDto(entity);
    }

    private void calculateOvernightStayAmount(TravelRequestDto travelRequestDto, OvernightStayRequestDto overnightStayRequestDto,
                                              OvernightStayEntity overnightStayEntity) {
        int quantityOfOvernightStay = getTotalQuantityOfNight(travelRequestDto);
        BigDecimal dailyAllowance = BigDecimal.valueOf(45);
        BigDecimal oneNightWithInvoice = dailyAllowance.multiply(BigDecimal.valueOf(20));
        BigDecimal oneNightWithoutInvoice = dailyAllowance.multiply(BigDecimal.valueOf(1.5));
        BigDecimal amountOfTotalOvernightsStayWithoutInvoice;


        if (overnightStayRequestDto.getInputQuantityOfOvernightStayWithoutInvoice() > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        } else {
            amountOfTotalOvernightsStayWithoutInvoice = oneNightWithoutInvoice.multiply(BigDecimal.valueOf(overnightStayRequestDto.getInputQuantityOfOvernightStayWithoutInvoice()));
            overnightStayEntity.setAmountOfTotalOvernightsStayWithoutInvoice(amountOfTotalOvernightsStayWithoutInvoice);
        }

        if (overnightStayRequestDto.getInputQuantityOfOvernightStayWithInvoice() > quantityOfOvernightStay) {
            throw new TravelException("Input quantity overnight stay more than quantity overnight stay");
        }

        if ((overnightStayRequestDto.getInputQuantityOfOvernightStayWithInvoice() +
                overnightStayRequestDto.getInputQuantityOfOvernightStayWithoutInvoice()) > quantityOfOvernightStay) {
            throw new TravelException("Total input numbers of overnight stay more than total overnight stay");
        }
        overnightStayEntity.setTotalInputQuantityOfOvernightStay(overnightStayRequestDto.getInputQuantityOfOvernightStayWithInvoice() +
                overnightStayRequestDto.getInputQuantityOfOvernightStayWithoutInvoice());
        overnightStayEntity.setOvernightStayAmount(amountOfTotalOvernightsStayWithoutInvoice
                .add(overnightStayRequestDto.getAmountOfTotalOvernightsStayWithInvoice()));
    }

    private int getTotalQuantityOfNight(TravelRequestDto travelRequestDto) {
        int night = 0;
        LocalDate startDate = travelRequestDto.getStartDate();
        LocalTime startTime = travelRequestDto.getStartTime();
        LocalDate endDate = travelRequestDto.getEndDate();
        LocalTime endTime = travelRequestDto.getEndTime();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        while (startDateTime.isBefore(endDateTime)) {
            LocalDateTime endOfCurrentNight = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);

            if (endDateTime.isBefore(endOfCurrentNight)) {
                endOfCurrentNight = endDateTime;
            }

            LocalDateTime startOfCurrentNight = startDateTime.withHour(21).withMinute(0).withSecond(0);

            if (startDateTime.isAfter(startOfCurrentNight)) {
                startOfCurrentNight = startDateTime;
            }

            if (Duration.between(startOfCurrentNight, endOfCurrentNight).toHours() >= 6) {
                night++;
            }

            startDateTime = startDateTime.plusDays(1).withHour(7).withMinute(0).withSecond(0);
        }
        return night;
    }
}
