package pl.sginko.travelexpense.domain.travelReport.exception;

public class TravelReportException extends RuntimeException {

    public TravelReportException(String message) {
        super(message);
    }

    public TravelReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
