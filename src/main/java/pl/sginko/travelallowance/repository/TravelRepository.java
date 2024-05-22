package pl.sginko.travelallowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelallowance.model.Entity.TravelEntity;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}
