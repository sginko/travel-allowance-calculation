package pl.sginko.travelexpense.logic.approval.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import pl.sginko.travelexpense.logic.approval.exception.ApprovalException;
import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

import java.util.Objects;


@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "approval")
public class ApprovalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelEntity travelEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity approver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;

    @Version
    private Long version;

    public ApprovalEntity(TravelEntity travelEntity, UserEntity approver, Roles role) {
        this.travelEntity = travelEntity;
        this.approver = approver;
        this.role = role;
        this.status = ApprovalStatus.PENDING;
    }

    public void updateStatus(ApprovalStatus newStatus) {
        this.status = newStatus;
    }

    public void validatePendingStatus(String approverEmail) {
        if (this.status != ApprovalStatus.PENDING) {
            throw new ApprovalException("Approval already processed for approver: " + approverEmail);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApprovalEntity)) return false;
        ApprovalEntity that = (ApprovalEntity) o;
        return Objects.equals(travelEntity.getId(), that.travelEntity.getId()) &&
                Objects.equals(approver.getId(), that.approver.getId()) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(travelEntity.getId(), approver.getId(), role);
    }
}
