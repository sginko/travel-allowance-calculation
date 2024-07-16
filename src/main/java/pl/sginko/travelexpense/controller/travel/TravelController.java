package pl.sginko.travelexpense.controller.travel;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.pdfDocument.PdfDocumentService;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.service.TravelService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/travels")
public class TravelController {
    private final TravelService travelService;
    private final PdfDocumentService pdfDocumentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TravelResponseDto calculateTravelExpenses(@RequestBody TravelRequestDto requestDto) {
        return travelService.calculateTravelExpenses(requestDto);
    }

    @PostMapping("/print/{id}")
    public ResponseEntity<Void> print(@PathVariable("id") Long id) {
        try {
            pdfDocumentService.generatePdfDocument(id);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.status(302)
                .header("Location", "/api/v1/travels/print/changed_template.pdf")
                .build();
    }

    @GetMapping("/print/changed_template.pdf")
    public ResponseEntity<InputStreamResource> getChangedTemplate() {
        try {
            File file = new File("src/main/resources/print/changed_template.pdf");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=changed_template.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
