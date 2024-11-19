package pl.sginko.travelexpense.domain.travelReport.exception;

public class TravelReportNotFoundException extends RuntimeException {

    public TravelReportNotFoundException(String message) {
        super(message);
    }
}
