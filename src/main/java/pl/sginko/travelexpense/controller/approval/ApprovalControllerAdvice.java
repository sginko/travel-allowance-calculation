package pl.sginko.travelexpense.controller.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sginko.travelexpense.domen.approval.exception.ApprovalException;

@RestControllerAdvice
public class ApprovalControllerAdvice {

    @ExceptionHandler(ApprovalException.class)
    public ResponseEntity<ApprovalControllerAdvice.Response> handleApprovalException(ApprovalException e) {
        ApprovalControllerAdvice.Response response = new ApprovalControllerAdvice.Response(e.getMessage());
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
