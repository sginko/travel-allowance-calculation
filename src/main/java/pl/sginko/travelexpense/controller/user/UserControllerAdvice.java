package pl.sginko.travelexpense.controller.user;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sginko.travelexpense.controller.travel.TravelControllerAdvice;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.user.exception.UserException;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserControllerAdvice {

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity handleEventException(ConstraintViolationException e) {
//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
//                .body(e.getConstraintViolations().stream()
//                        .map(error -> error.getMessageTemplate()).collect(Collectors.joining(",", "", ""))
//                        .replace(",", "\n"));
//    }
//
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(error -> error.getPropertyPath() + ": " + error.getMessage())
                .collect(Collectors.joining("\n"));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
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

    @ExceptionHandler(UserException.class)
    public ResponseEntity<UserControllerAdvice.Response> handleUserException(UserException e) {
        UserControllerAdvice.Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String message;
    }
}
