package pl.sginko.travelexpense.logic.overnightStay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.overnightStay.entity.OvernightStayEntity;

public interface OvernightStayRepository extends JpaRepository<OvernightStayEntity, Long> {
}