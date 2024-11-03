package pl.sginko.travelexpense.logic.travelexpense.travel.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {

    Optional<TravelEntity> findByTechId (UUID techId);

    List<TravelEntity> findAllByUserEntity_Email(String email);

    void deleteAllByUserEntity_Email(String email);

    Optional<TravelEntity> findByTechIdAndUserEntity_Email(UUID techId, String email);
}
