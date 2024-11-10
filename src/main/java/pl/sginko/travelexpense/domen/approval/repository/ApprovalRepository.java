package pl.sginko.travelexpense.domen.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.domen.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.domen.auth.entity.Roles;
import pl.sginko.travelexpense.domen.travelexpense.travel.entity.TravelEntity;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {

    boolean existsByTravelEntityAndRole(TravelEntity travelEntity, Roles role);
}
