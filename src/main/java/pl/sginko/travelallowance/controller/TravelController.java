package pl.sginko.travelallowance.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelallowance.PdfDocumentPrinter;
import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.service.TravelService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/api/v1/travels")
class TravelController {
    private final TravelService travelService;
    private final PdfDocumentPrinter pdfDocumentPrinter;

    TravelController(TravelService travelService, PdfDocumentPrinter pdfDocumentPrinter) {
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
    public ResponseEntity<InputStreamResource> getChangedTemplate() throws IOException {
        File file = new File("src/main/resources/templates/files/changed_template.pdf");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=changed_template.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


//    @PostMapping("/print")
//    public ResponseEntity<byte[]> print(@ModelAttribute TravelResponseDto travelResponseDto) {
//        Long id = travelResponseDto.getId();
//        byte[] pdfBytes;
//        try {
//            pdfBytes = pdfDocumentPrinter.generatePdfDocument(id);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "inline; filename=report.pdf");
//
//        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//    }

    @GetMapping
    public String getTravelForm() {
        return "travel-calculator";
    }


    @PostMapping
    public String calculateTravelExpenses(@ModelAttribute TravelRequestDto travelRequestDto, Model model) {
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);
        model.addAttribute("travelResponse", travelResponseDto);
        return "results";
    }
}