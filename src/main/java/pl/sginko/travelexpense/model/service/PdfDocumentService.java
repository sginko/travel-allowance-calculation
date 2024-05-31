package pl.sginko.travelexpense.model.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.Entity.TravelEntity;
import pl.sginko.travelexpense.model.repository.TravelRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class PdfDocumentService {
    private final TravelRepository travelRepository;

    public PdfDocumentService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public void generatePdfDocument(Long id) throws IOException {
        TravelEntity businessTrip = travelRepository.findById(id).orElseThrow(() -> new RuntimeException("Business trip not found"));

        Map<String, String> replacements = Map.of(
                "startDate", businessTrip.getStartDate().toString(),
                "startTime", businessTrip.getStartTime().toString(),
                "endDate", businessTrip.getEndDate().toString(),
                "endTime", businessTrip.getEndTime().toString(),
                "countBreakfast", String.valueOf(businessTrip.getNumberOfBreakfasts()),
                "countLunch", String.valueOf(businessTrip.getNumberOfLunches()),
                "countDinner", String.valueOf(businessTrip.getNumberOfDinners()),
                "totalAmount", String.valueOf(businessTrip.getTotalAmount()),
                "dietAmount", String.valueOf(businessTrip.getDietAmount()),
                "foodAmount", String.valueOf(businessTrip.getFoodAmount())
        );

        String templatePath = "src/main/resources/templates/files/template.pdf";
        String outputPath = "src/main/resources/templates/files/changed_template.pdf";

        fillTemplate(templatePath, outputPath, replacements);
    }

//    private void fillTemplate(String templatePath, String outputPath, Map<String, String> data) throws IOException {
//        try (PDDocument document = PDDocument.load(new FileInputStream(templatePath))) {
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//
//            if (acroForm != null) {
//                for (Map.Entry<String, String> entry : data.entrySet()) {
//                    PDField field = acroForm.getField(entry.getKey());
//                    if (field != null) {
//                        field.setValue(entry.getValue());
//                    }
//                }
//                acroForm.flatten();
//            }
//
//            document.save(outputPath);
//        }
//    }

    private void fillTemplate(String templatePath, String outputPath, Map<String, String> data) throws IOException {
        try (PDDocument document = PDDocument.load(new FileInputStream(templatePath))) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                for (PDField field : acroForm.getFields()) {
                    System.out.println("Field name: " + field.getFullyQualifiedName());
                }

                for (Map.Entry<String, String> entry : data.entrySet()) {
                    PDField field = acroForm.getField(entry.getKey());
                    if (field != null) {
                        field.setValue(entry.getValue());
                        System.out.println("Setting field " + entry.getKey() + " to " + entry.getValue());
                    } else {
                        System.out.println("Field " + entry.getKey() + " not found in the form.");
                    }
                }
                acroForm.flatten();
            } else {
                System.out.println("AcroForm is null.");
            }
            document.save(outputPath);
        }
    }
}