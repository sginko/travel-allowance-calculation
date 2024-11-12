package pl.sginko.travelexpense.logic.actionLog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.actionLog.entity.ActionLogEntity;

public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {
}
