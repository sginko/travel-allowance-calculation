package pl.sginko.travelexpense.controller.travel;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.pdfDocument.service.PdfDocumentService;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportRequestDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.dto.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.exception.TravelReportNotFoundException;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.service.TravelReportService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    public ResponseEntity<Void> generateTravelExpenseReportPdf(@PathVariable("techId") UUID techId) {
        try {
            pdfDocumentService.generateTravelExpenseReportPdf(techId);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/api/v1/travels/print/changed_template.pdf")
                    .build();
        } catch (TravelReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PostMapping("/print/{techId}")
//    public ResponseEntity<Void> generateTravelExpenseReportPdf(@PathVariable("techId") UUID techId) {
//        try {
//            pdfDocumentService.generateTravelExpenseReportPdf(techId);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//        return ResponseEntity.status(302)
//                .header("Location", "/api/v1/travels/print/changed_template.pdf")
//                .build();
//    }

    @GetMapping("/print/changed_template.pdf")
    public ResponseEntity<InputStreamResource> getTravelExpenseReportPdf() {
        try {
            File file = new File("src/main/resources/print/changed_template.pdf"); //without Docker
//            File file = new File("/app/resources/print/changed_template.pdf"); //with Docker
            if (!file.exists() || !file.canRead()) {
                throw new IOException("File not accessible");
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=changed_template.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/print/changed_template.pdf")
//    public ResponseEntity<InputStreamResource> getTravelExpenseReportPdf() {
//        try {
//            File file = new File("src/main/resources/print/changed_template.pdf"); //without Docker
////            File file = new File("/app/resources/print/changed_template.pdf"); //with Docker
//            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "inline; filename=changed_template.pdf");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(file.length())
//                    .contentType(MediaType.APPLICATION_PDF)
//                    .body(resource);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//    }
}
