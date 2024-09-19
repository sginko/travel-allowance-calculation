package pl.sginko.travelexpense.controller.user;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sginko.travelexpense.controller.travel.TravelControllerAdvice;
import pl.sginko.travelexpense.logic.user.exception.UserException;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<UserControllerAdvice.Response> handleUserException(UserException e) {
        UserControllerAdvice.Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<TravelControllerAdvice.Response> handleConstraintViolationException(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(error -> error.getMessageTemplate())
                .collect(Collectors.joining("\n"));
        TravelControllerAdvice.Response response = new TravelControllerAdvice.Response(errors);
        return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TravelControllerAdvice.Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        TravelControllerAdvice.Response response = new TravelControllerAdvice.Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<TravelControllerAdvice.Response> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getMostSpecificCause();
        String message;
        if (cause instanceof DateTimeParseException) {
            message = "Invalid date and time format: " + cause.getMessage();
            return new ResponseEntity<>(new TravelControllerAdvice.Response(message), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            message = "Malformed JSON request";
            return new ResponseEntity<>(new TravelControllerAdvice.Response(message), HttpStatus.BAD_REQUEST);
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
