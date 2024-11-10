package pl.sginko.travelexpense.domen.travelexpense.travel.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.sginko.travelexpense.domen.travelexpense.travel.exception.TravelException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TravelEntityTest {
    @Test
    void should_throw_exception_when_end_date_is_before_start_date() {
        // GIVEN
        LocalDate startDate = LocalDate.of(2024, 11, 5);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalDate endDate = LocalDate.of(2024, 11, 4);
        LocalTime endTime = LocalTime.of(9, 0);

        // WHEN
        Executable e = () -> new TravelEntity("CityA", "CityB",
                startDate, startTime, endDate, endTime,
                null, BigDecimal.ZERO, BigDecimal.ZERO);
        // THEN
        assertThrows(TravelException.class, e);
    }
//
//    @Test
//    void shouldUpdateDietEntity() {
//        // GIVEN
//        TravelEntity travelEntity = createTravelEntity();
//        DietEntity dietEntity = createDietEntity(BigDecimal.valueOf(200));
//
//        // WHEN
//        travelEntity.updateDietEntity(dietEntity);
//
//        // THEN
//        assertThat(travelEntity.getDietEntity()).isEqualTo(dietEntity);
//    }
//
//    @Test
//    void shouldUpdateOvernightStayEntity() {
//        // GIVEN
//        TravelEntity travelEntity = createTravelEntity();
//        OvernightStayEntity overnightStayEntity = createOvernightStayEntity(BigDecimal.valueOf(150));
//
//        // WHEN
//        travelEntity.updateOvernightStayEntity(overnightStayEntity);
//
//        // THEN
//        assertThat(travelEntity.getOvernightStayEntity()).isEqualTo(overnightStayEntity);
//    }
//
//    @Test
//    void shouldUpdateTransportCostEntity() {
//        // GIVEN
//        TravelEntity travelEntity = createTravelEntity();
//        TransportCostEntity transportCostEntity = createTransportCostEntity(BigDecimal.valueOf(300));
//
//        // WHEN
//        travelEntity.updateTransportCostEntity(transportCostEntity);
//
//        // THEN
//        assertThat(travelEntity.getTransportCostEntity()).isEqualTo(transportCostEntity);
//    }
//
//    @Test
//    void shouldCalculateTotalAmount() {
//        // GIVEN
//        TravelEntity travelEntity = createTravelEntity();
//        DietEntity dietEntity = createDietEntity(BigDecimal.valueOf(200));
//        OvernightStayEntity overnightStayEntity = createOvernightStayEntity(BigDecimal.valueOf(150));
//        TransportCostEntity transportCostEntity = createTransportCostEntity(BigDecimal.valueOf(300));
//
//        travelEntity.updateDietEntity(dietEntity);
//        travelEntity.updateOvernightStayEntity(overnightStayEntity);
//        travelEntity.updateTransportCostEntity(transportCostEntity);
//
//        // WHEN
//        travelEntity.updateTotalAmount();
//
//        // THEN
//        assertThat(travelEntity.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(600)); // 200 + 150 + 300 + 50 - 100 = 600
//    }

//    // Вспомогательные методы для создания сущностей
//    private UserEntity createUserEntity() {
//        // Создайте объект UserEntity с необходимыми данными
//        return new UserEntity(/* инициализация */);
//    }
//
//    private TravelEntity createTravelEntity() {
//        return new TravelEntity("CityA", "CityB",
//                LocalDate.of(2024, 11, 5), LocalTime.of(10, 0),
//                LocalDate.of(2024, 11, 6), LocalTime.of(15, 0),
//                createUserEntity(), BigDecimal.ZERO, BigDecimal.ZERO);
//    }
//
//    private DietEntity createDietEntity(BigDecimal dietAmount) {
//        // Создайте объект DietEntity с использованием рефлексии или вспомогательных методов
//        DietEntity dietEntity = new DietEntity();
//        dietEntity.setDietAmount(dietAmount);
//        return dietEntity;
//    }
//
//    private OvernightStayEntity createOvernightStayEntity(BigDecimal overnightStayAmount) {
//        // Создайте объект OvernightStayEntity с использованием рефлексии или вспомогательных методов
//        OvernightStayEntity overnightStayEntity = new OvernightStayEntity();
//        overnightStayEntity.setOvernightStayAmount(overnightStayAmount);
//        return overnightStayEntity;
//    }
//
//    private TransportCostEntity createTransportCostEntity(BigDecimal transportCostAmount) {
//        // Создайте объект TransportCostEntity с использованием рефлексии или вспомогательных методов
//        TransportCostEntity transportCostEntity = new TransportCostEntity();
//        transportCostEntity.setTransportCostAmount(transportCostAmount);
//        return transportCostEntity;
//    }

}