package pl.sginko.travelexpense.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.model.service.PdfDocumentService;
import pl.sginko.travelexpense.model.Dto.TravelRequestDto;
import pl.sginko.travelexpense.model.Dto.TravelResponseDto;
import pl.sginko.travelexpense.model.service.TravelService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/api/v1/travels")
public class TravelController {
    private final TravelService travelService;
    private final PdfDocumentService pdfDocumentPrinter;

    public TravelController(TravelService travelService, PdfDocumentService pdfDocumentPrinter) {
        this.travelService = travelService;
        this.pdfDocumentPrinter = pdfDocumentPrinter;
    }

    @PostMapping("/print")
    public ResponseEntity<Void> print(@RequestParam("id") Long id) {
        try {
            pdfDocumentPrinter.generatePdfDocument(id);
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
            File file = new File("src/main/resources/templates/files/changed_template.pdf");
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

    @GetMapping
    public String getTravelForm() {
        return "travel-calculator";
    }

    @PostMapping
    public String calculateTravelExpenses(@ModelAttribute TravelRequestDto requestDto, Model model) {
        TravelResponseDto responseDto = travelService.calculateTravelExpenses(requestDto);
        model.addAttribute("travelResponse", responseDto);
        return "results";
    }
}