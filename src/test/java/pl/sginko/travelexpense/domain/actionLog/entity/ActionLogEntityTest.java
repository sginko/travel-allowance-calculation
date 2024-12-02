/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
