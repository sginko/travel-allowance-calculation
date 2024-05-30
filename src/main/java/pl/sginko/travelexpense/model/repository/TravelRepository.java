package pl.sginko.travelexpense.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.model.Entity.TravelEntity;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}