package pl.sginko.travelexpense.controller.travel;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sginko.travelexpense.logic.diet.exception.DietException;
import pl.sginko.travelexpense.logic.overnightStay.exception.OvernightStayException;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TravelControllerAdvice {

//    @ExceptionHandler(TravelException.class)
//    public ResponseEntity<Map<String, String>> handleEventException2(TravelException e) {
//        Map<String, String> error = new HashMap<>();
//        error.put("message", "Error: " + e.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(DietException.class)
//    public ResponseEntity<Map<String, String>> handleEventException(DietException e) {
//        Map<String, String> error = new HashMap<>();
//        error.put("message", "Error: " + e.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(OvernightStayException.class)
//    public ResponseEntity<Map<String, String>> handleEventException(OvernightStayException e) {
//        Map<String, String> error = new HashMap<>();
//        error.put("message", "Error: " + e.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Map<String, String>> handleEventException(ConstraintViolationException e) {
//        String errors = e.getConstraintViolations().stream()
//                .map(error -> error.getMessageTemplate()).collect(Collectors.joining(",", "", ""))
//                .replace(",", "\n");
//        Map<String, String> error = new HashMap<>();
//        error.put("message", errors);
//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
//        Throwable cause = e.getMostSpecificCause();
//        Map<String, String> error = new HashMap<>();
//        if (cause instanceof DateTimeParseException) {
//            error.put("message", "Invalid date and time format: " + cause.getMessage());
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
//        }
//        error.put("message", "Malformed JSON request");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }

    @ExceptionHandler(TravelException.class)
    public ResponseEntity<String> handleTravelException(TravelException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
    }

    @ExceptionHandler(DietException.class)
    public ResponseEntity<String> handleDietException(DietException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
    }

    @ExceptionHandler(OvernightStayException.class)
    public ResponseEntity<String> handleOvernightStayException(OvernightStayException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(error -> error.getMessageTemplate()).collect(Collectors.joining("\n"));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errors);
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
