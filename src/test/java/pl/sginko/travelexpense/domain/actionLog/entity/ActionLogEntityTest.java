package pl.sginko.travelexpense.domain.actionLog.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActionLogEntityTest {
    private ActionLogEntity actionLogEntity;

    private static final String INITIAL_MESSAGE = "Initial Message";
    private static final Long INITIAL_TRAVEL_REPORT_ID = 1L;
    private static final Long INITIAL_APPROVER_ID = 2L;
    private static final long INITIAL_TIMESTAMP = System.currentTimeMillis();

    @BeforeEach
    void setUp() {
        actionLogEntity = new ActionLogEntity(INITIAL_MESSAGE, INITIAL_TRAVEL_REPORT_ID, INITIAL_APPROVER_ID, INITIAL_TIMESTAMP);
    }

    @Test
    void should_update_message_correctly() {
        // GIVEN
        String newMessage = "Updated Message";

        // WHEN
        actionLogEntity.updateMessage(newMessage);

        // THEN
        assertThat(actionLogEntity.getMessage()).isEqualTo(newMessage);
    }

    @Test
    void should_update_travel_id_correctly() {
        // GIVEN
        Long newTravelId = 10L;

        // WHEN
        actionLogEntity.updateTravelId(newTravelId);

        // THEN
        assertThat(actionLogEntity.getTravelReportId()).isEqualTo(newTravelId);
    }

    @Test
    void should_update_approver_id_correctly() {
        // GIVEN
        Long newApproverId = 20L;

        // WHEN
        actionLogEntity.updateApproverId(newApproverId);

        // THEN
        assertThat(actionLogEntity.getApproverId()).isEqualTo(newApproverId);
    }

    @Test
    void should_update_timestamp_correctly() {
        // GIVEN
        long newTimestamp = System.currentTimeMillis();

        // WHEN
        actionLogEntity.updateTimestamp(newTimestamp);

        // THEN
        assertThat(actionLogEntity.getTimestamp()).isEqualTo(newTimestamp);
    }
}
