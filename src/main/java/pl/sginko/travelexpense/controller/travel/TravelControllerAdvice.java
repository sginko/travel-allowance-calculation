package pl.sginko.travelexpense.controller.travel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.sginko.travelexpense.model.travel.TravelException;

@ControllerAdvice
public class TravelControllerAdvice {

    @ExceptionHandler(TravelException.class)
    public ResponseEntity handleEventException(TravelException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
