package pl.sginko.travelexpense.logic.actionLog.service;

public interface ActionLogService {

    void logAction(String message, Long travelId, Long approverId);
}
