package pl.sginko.travelexpense.controller.travel;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sginko.travelexpense.logic.diet.exception.DietException;
import pl.sginko.travelexpense.logic.overnightStay.exception.OvernightStayException;
import pl.sginko.travelexpense.logic.transport.TransportException;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TravelControllerAdvice {

    @ExceptionHandler(TravelException.class)
    public ResponseEntity<Response> handleTravelException(TravelException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DietException.class)
    public ResponseEntity<Response> handleDietException(DietException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(OvernightStayException.class)
    public ResponseEntity<Response> handleOvernightStayException(OvernightStayException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(TransportException.class)
    public ResponseEntity<Response> handleTransportException(TransportException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response> handleConstraintViolationException(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(error -> error.getMessageTemplate())
                .collect(Collectors.joining("\n"));
        Response response = new Response(errors);
        return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getMostSpecificCause();
        String message;
        if (cause instanceof DateTimeParseException) {
            message = "Invalid date and time format: " + cause.getMessage();
            return new ResponseEntity<>(new Response(message), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            message = "Malformed JSON request";
            return new ResponseEntity<>(new Response(message), HttpStatus.BAD_REQUEST);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String message;
    }
}
