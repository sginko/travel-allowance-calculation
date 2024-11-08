package pl.sginko.travelexpense.logic.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.logic.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.util.List;
import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {

    boolean existsByTravelEntityAndRole(TravelEntity travelEntity, Roles role);

    List<ApprovalEntity> findByTravelEntity(TravelEntity travelEntity);
}
