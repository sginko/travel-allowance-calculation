package pl.sginko.travelexpense.logic.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}
