package pl.sginko.travelexpense.logic.diet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.diet.entity.DietEntity;

public interface DietRepository extends JpaRepository<DietEntity, Long> {
}