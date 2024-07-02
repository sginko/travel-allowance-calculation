package pl.sginko.travelexpense.model.overnightStay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.model.overnightStay.entity.OvernightStayEntity;

public interface OvernightStayRepository extends JpaRepository<OvernightStayEntity, Long> {
}
