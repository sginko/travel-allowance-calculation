package pl.sginko.travelexpense.controller.travel;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.pdfDocument.PdfDocumentService;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;
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

//    @GetMapping("/download")
//    public ResponseEntity<byte[]> downloadPdf() {
//        try {
//            File file = new File("src/main/resources/print/changed_template.pdf");
//            InputStream inputStream = new FileInputStream(file);
//            byte[] fileContent = inputStream.readAllBytes();
//            inputStream.close();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=changed_template.pdf");
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
//
//            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
}
