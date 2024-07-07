package pl.sginko.travelexpense.controller.user;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.sginko.travelexpense.logic.user.exception.UserException;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity handleEventException(UserException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleEventException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(e.getConstraintViolations().stream()
                        .map(error -> error.getMessageTemplate()).collect(Collectors.joining(",", "", ""))
                        .replace(",", "\n"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getMostSpecificCause();
        if (cause instanceof DateTimeParseException) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Invalid date and time format: " + cause.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed JSON request");
    }
}