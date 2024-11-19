package pl.sginko.travelexpense.domain.travelReport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TravelReportRepository extends JpaRepository<TravelReportEntity, Long> {

    Optional<TravelReportEntity> findByTechId(UUID techId);

    List<TravelReportEntity> findAllByUserEntity_Email(String email);

    List<TravelReportEntity> findByStatusIn(List<TravelReportStatus> statuses);

    List<TravelReportEntity> findByStartDateBefore(LocalDate cutoffDate);

    Optional<TravelReportEntity> findByTechIdAndUserEntity_Email(UUID techId, String email);
}
