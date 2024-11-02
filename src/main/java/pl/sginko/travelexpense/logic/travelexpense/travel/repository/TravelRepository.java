package pl.sginko.travelexpense.logic.travelexpense.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.travelexpense.travel.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.util.List;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {

    List<TravelEntity> findAllByUserEntity_Email(String email);
}

