package pl.sginko.travelexpense.logic.approval.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.approval.exception.ApprovalException;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;

@EqualsAndHashCode(of = {"travelReportEntity", "approver", "role"})
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "approval")
public class ApprovalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "travel_report_id", nullable = false)
    private TravelReportEntity travelReportEntity;

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

    public ApprovalEntity(TravelReportEntity travelReportEntity, UserEntity approver, Roles role) {
        this.travelReportEntity = travelReportEntity;
        this.approver = approver;
        this.role = role;
        this.status = ApprovalStatus.PENDING;
    }

    public void updateStatus(ApprovalStatus newStatus) {
        this.status = newStatus;
    }

    public void validateApprovalStatus() {
        if (this.status != ApprovalStatus.PENDING) {
            throw new ApprovalException("Approval already processed.");
        }
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ApprovalEntity that = (ApprovalEntity) o;
//
//        return Objects.equals(travelEntity, that.travelEntity) &&
//                Objects.equals(approver, that.approver) &&
//                role == that.role;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(travelEntity, approver, role);
//    }
}
