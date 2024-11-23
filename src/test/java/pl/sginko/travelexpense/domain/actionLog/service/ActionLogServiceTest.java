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
package pl.sginko.travelexpense.domain.actionLog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sginko.travelexpense.domain.actionLog.entity.ActionLogEntity;
import pl.sginko.travelexpense.domain.actionLog.repository.ActionLogRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActionLogServiceTest {
    @Mock
    private ActionLogRepository actionLogRepository;

    @InjectMocks
    private ActionLogServiceImpl actionLogService;

    @Test
    void should_log_action_successfully() {
        // GIVEN
        String message = "Action logged";
        Long travelId = 1L;
        Long approverId = 2L;

        // WHEN
        actionLogService.logAction(message, travelId, approverId);

        // THEN
        verify(actionLogRepository, times(1)).save(any(ActionLogEntity.class));
    }

    @Test
    void should_log_action_with_correct_details() {
        // GIVEN
        String message = "Action logged";
        Long travelId = 1L;
        Long approverId = 2L;

        // WHEN
        actionLogService.logAction(message, travelId, approverId);

        // THEN
        verify(actionLogRepository, times(1)).save(argThat(log ->
                log.getMessage().equals(message) &&
                        log.getTravelReportId().equals(travelId) &&
                        log.getApproverId().equals(approverId) &&
                        log.getTimestamp() > 0));
    }
}
