package pl.sginko.travelexpense.domain.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.domain.user.entity.Roles;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {

    boolean existsByTravelReportEntityAndRole(TravelReportEntity travelReportEntity, Roles role);
}
