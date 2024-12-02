/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.controller.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sginko.travelexpense.domain.approval.exception.ApprovalException;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportNotFoundException;

@RestControllerAdvice
public class ApprovalControllerAdvice {

    @ExceptionHandler(ApprovalException.class)
    public ResponseEntity<Response> handleApprovalException(ApprovalException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
    }

    @ExceptionHandler(TravelReportNotFoundException.class)
    public ResponseEntity<Response> handleTravelReportNotFoundException(TravelReportNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(ex.getMessage()));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String message;
    }
}
