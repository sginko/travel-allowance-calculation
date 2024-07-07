package pl.sginko.travelexpense.controller.travel;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.pdfDocument.PdfDocumentService;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.service.TravelService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/travels")
public class TravelController {
    private final TravelService travelService;
    private final PdfDocumentService pdfDocumentPrinter;

//    @PostMapping("/print")
//    public ResponseEntity<Void> print(@RequestParam("id") Long id) {
//        try {
//            pdfDocumentPrinter.generatePdfDocument(id);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//        return ResponseEntity.status(302)
//                .header("Location", "/api/v1/travels/print/changed_template.pdf")
//                .build();
//    }
//
//    @GetMapping("/print/changed_template.pdf")
//    public ResponseEntity<InputStreamResource> getChangedTemplate() {
//        try {
//            File file = new File("src/main/resources/templates/files/changed_template.pdf");
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
//
//    @GetMapping
//    public String getTravelForm(@RequestParam(value = "pesel", required = false) Long pesel, Model model) {
//        model.addAttribute("pesel", pesel);
//        return "travel-calculator";
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void calculateTravelExpenses(@RequestBody TravelRequestDto requestDto) {
        travelService.calculateTravelExpenses(requestDto);
    }
}
