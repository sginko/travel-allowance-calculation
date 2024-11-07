package pl.sginko.travelexpense.logic.approval.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;

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

    public ApprovalEntity(TravelEntity travelEntity, UserEntity approver, Roles role) {
        this.travelEntity = travelEntity;
        this.approver = approver;
        this.role = role;
        this.status = ApprovalStatus.PENDING;
    }

    public void updateStatus(ApprovalStatus newStatus) {
        this.status = newStatus;
    }
}
