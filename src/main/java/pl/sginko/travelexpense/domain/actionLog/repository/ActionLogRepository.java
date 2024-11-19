package pl.sginko.travelexpense.domain.actionLog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.domain.actionLog.entity.ActionLogEntity;

public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {
}
