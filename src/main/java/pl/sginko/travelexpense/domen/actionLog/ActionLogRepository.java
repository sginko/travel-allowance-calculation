package pl.sginko.travelexpense.domen.actionLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {
}
