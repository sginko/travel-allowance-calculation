package pl.sginko.travelexpense.domain.approval.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.domain.approval.exception.ApprovalException;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.entity.UserRoles;

@EqualsAndHashCode(of = {"travelReportEntity", "approver", "role"})
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "approval")
public class ApprovalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "travel_id", nullable = false)
    private TravelReportEntity travelReportEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity approver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoles role;

    @Version
    private Long version;

    public ApprovalEntity(TravelReportEntity travelReportEntity, UserEntity approver, UserRoles role) {
        this.travelReportEntity = travelReportEntity;
        this.approver = approver;
        this.role = role;
        this.status = ApprovalStatus.PENDING;
    }

    public void updateApprovalStatus(ApprovalStatus newStatus) {
        this.status = newStatus;
    }

    public void checkIfStatusPending() {
        if (this.status != ApprovalStatus.PENDING) {
            throw new ApprovalException("Approval already processed.");
        }
    }
}
