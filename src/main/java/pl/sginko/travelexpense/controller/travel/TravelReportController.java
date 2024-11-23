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
package pl.sginko.travelexpense.controller.travel;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.common.pdfDocument.exception.PdfDocumentException;
import pl.sginko.travelexpense.common.pdfDocument.service.PdfDocumentService;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportRequestDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.service.travelReport.TravelReportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/travels")
class TravelReportController {
    private final TravelReportService travelReportService;
    private final PdfDocumentService pdfDocumentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TravelReportSubmissionResponseDto createTravelExpenseReport(@RequestBody TravelReportRequestDto requestDto) {
        return travelReportService.createTravelExpenseReport(requestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TravelReportResponseDto> getUserTravelExpenseReports() {
        return travelReportService.getUserTravelExpenseReports();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{techId}")
    public TravelReportSubmissionResponseDto getTravelExpenseReportById(@PathVariable("techId") UUID techId) {
        return travelReportService.getTravelExpenseReportById(techId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/update/{techId}")
    public void updateTravelExpenseReportById(@PathVariable("techId") UUID techId, @RequestBody JsonPatch patch) {
        travelReportService.updateTravelExpenseReportById(techId, patch);
    }

    @PostMapping("/print/{techId}")
    public ResponseEntity<InputStreamResource> generateTravelExpenseReportPdf(@PathVariable("techId") UUID techId) {
        try {
            ByteArrayOutputStream pdfStream = pdfDocumentService.generateTravelExpenseReportPdfAsStream(techId);

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfStream.toByteArray()));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=travel_expense_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfStream.size())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (TravelReportException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (PdfDocumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
