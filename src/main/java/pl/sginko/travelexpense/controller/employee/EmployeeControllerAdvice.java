package pl.sginko.travelexpense.controller.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.sginko.travelexpense.model.employee.EmployeeException;

@ControllerAdvice
public class EmployeeControllerAdvice {

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity handleEventException(EmployeeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
