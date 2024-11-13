package pl.sginko.travelexpense.logic.travelexpense.travel.exception;

public class TravelException extends RuntimeException {

    public TravelException(String message) {
        super(message);
    }

    public TravelException(String message, Throwable cause) {
        super(message, cause);
    }
}
