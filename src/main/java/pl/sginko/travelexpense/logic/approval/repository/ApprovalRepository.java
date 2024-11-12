package pl.sginko.travelexpense.logic.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {

    boolean existsByTravelEntityAndRole(TravelEntity travelEntity, Roles role);
}
