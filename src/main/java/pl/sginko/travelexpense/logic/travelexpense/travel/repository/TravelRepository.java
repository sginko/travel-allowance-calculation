package pl.sginko.travelexpense.logic.travelexpense.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {

    Optional<TravelEntity> findByTechId(UUID techId);

    List<TravelEntity> findAllByUserEntity_Email(String email);

    List<TravelEntity> findByStatusIn(List<TravelStatus> statuses);
}
