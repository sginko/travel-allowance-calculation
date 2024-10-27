package pl.sginko.travelexpense.logic.travelexpense.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}
