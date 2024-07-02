package pl.sginko.travelexpense.model.diet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.model.diet.entity.DietEntity;

public interface DietRepository extends JpaRepository<DietEntity, Long> {
}
