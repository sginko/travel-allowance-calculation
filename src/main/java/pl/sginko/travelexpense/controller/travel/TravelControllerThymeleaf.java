//package pl.sginko.travelexpense.controller.travel;
//
//import jakarta.validation.Valid;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import pl.sginko.travelexpense.logic.diet.dto.DietDto;
//import pl.sginko.travelexpense.logic.overnightStay.dto.OvernightStayDto;
//import pl.sginko.travelexpense.logic.pdfDocument.PdfDocumentService;
//import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
//import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
//import pl.sginko.travelexpense.logic.travel.service.TravelService;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.math.BigDecimal;
//
//@Controller
//@RequestMapping("/api/v1/travels")
//class TravelControllerThymeleaf {
//    private final TravelService travelService;
//    private final PdfDocumentService pdfDocumentPrinter;
//
//    public TravelControllerThymeleaf(TravelService travelService, PdfDocumentService pdfDocumentPrinter) {
//        this.travelService = travelService;
//        this.pdfDocumentPrinter = pdfDocumentPrinter;
//    }
//
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
//
//    @PostMapping
//    public String calculateTravelExpenses(@Valid @ModelAttribute TravelRequestDto requestDto, Model model) {
//        // Проверка на null и установка значений по умолчанию
//        if (requestDto.getDietDto() == null) {
//            requestDto.setDietDto(DietDto.builder()
//                    .dailyAllowance(new BigDecimal("45"))  // Установите значение по умолчанию
//                    .numberOfBreakfasts(0)
//                    .numberOfLunches(0)
//                    .numberOfDinners(0)
//                    .build());
//        }
//        if (requestDto.getOvernightStayDto() == null) {
//            requestDto.setOvernightStayDto(OvernightStayDto.builder()
//                    .inputQuantityOfOvernightStayWithoutInvoice(0)
//                    .amountOfTotalOvernightsStayWithoutInvoice(BigDecimal.ZERO)
//                    .inputQuantityOfOvernightStayWithInvoice(0)
//                    .amountOfTotalOvernightsStayWithInvoice(BigDecimal.ZERO)
//                    .build());
//        }
//
//        TravelResponseDto responseDto = travelService.calculateTravelExpenses(requestDto);
//        model.addAttribute("travelResponse", responseDto);
//        return "results";
//    }
//}
