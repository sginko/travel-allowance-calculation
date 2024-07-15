package pl.sginko.travelexpense.model.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}
