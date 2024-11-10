package pl.sginko.travelexpense.domen.travelexpense.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {

    Optional<TravelEntity> findByTechId(UUID techId);

    List<TravelEntity> findAllByUserEntity_Email(String email);

    List<TravelEntity> findByStatusIn(List<TravelStatus> statuses);

    List<TravelEntity> findByStartDateBefore(LocalDate cutoffDate);
}
