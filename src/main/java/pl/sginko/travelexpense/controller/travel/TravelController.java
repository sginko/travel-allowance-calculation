package pl.sginko.travelexpense.controller.travel;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.pdfDocument.PdfDocumentService;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.service.TravelService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/travels")
public class TravelController {
    private final TravelService travelService;
    private final PdfDocumentService pdfDocumentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TravelResponseDto calculateTravelExpenses(@RequestBody @Valid TravelRequestDto requestDto) {
        return travelService.calculateTravelExpenses(requestDto);
    }

    @PostMapping("/print/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable Long id) {
        try {
            pdfDocumentService.generatePdfDocument(id);
            return ResponseEntity.ok("PDF document generated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF document: " + e.getMessage());
        }
    }
}
