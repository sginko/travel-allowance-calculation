package pl.sginko.travelexpense.domen.actionLog;

public interface ActionLogService {

    void logAction(String message, Long travelId, Long approverId);
}
