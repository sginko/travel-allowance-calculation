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
