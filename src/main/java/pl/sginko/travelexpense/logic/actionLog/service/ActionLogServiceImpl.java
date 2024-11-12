package pl.sginko.travelexpense.logic.actionLog.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.actionLog.entity.ActionLogEntity;
import pl.sginko.travelexpense.logic.actionLog.repository.ActionLogRepository;

@AllArgsConstructor
@Service
public class ActionLogServiceImpl implements ActionLogService {
    private final ActionLogRepository actionLogRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String message, Long travelId, Long approverId) {
        ActionLogEntity log = new ActionLogEntity(message, travelId, approverId, System.currentTimeMillis());

        actionLogRepository.save(log);
    }
}
