package pl.sginko.travelexpense.logic.travelexpense.travelReport.exception;

public class TravelReportException extends RuntimeException {

    public TravelReportException(String message) {
        super(message);
    }

    public TravelReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
