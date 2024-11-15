package pl.sginko.travelexpense.logic.actionLog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "action_log")
public class ActionLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private Long travelReportId;
    private Long approverId;
    private long timestamp;


    public ActionLogEntity(String message, Long travelReportId, Long approverId, long timestamp) {
        this.message = message;
        this.travelReportId = travelReportId;
        this.approverId = approverId;
        this.timestamp = timestamp;
    }

    public void updateMessage(String message) {
        this.message = message;
    }

    public void updateTravelId(Long travelReportId) {
        this.travelReportId = travelReportId;
    }

    public void updateApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public void updateTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
