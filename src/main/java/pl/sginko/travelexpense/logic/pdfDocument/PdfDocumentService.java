package pl.sginko.travelexpense.logic.pdfDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travel.model.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travel.repository.TravelRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static java.util.Map.entry;

@Service
public class PdfDocumentService {
    private final TravelRepository travelRepository;

    public PdfDocumentService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public void generatePdfDocument(Long id) throws IOException {
        TravelEntity travel = travelRepository.findById(id).orElseThrow(() -> new TravelException("Travel not found"));

        Map<String, String> replacements = Map.ofEntries(
                entry("fullName", travel.getEmployeeEntity().getFirstName() + " " + travel.getEmployeeEntity().getSecondName()),
                entry("position", travel.getEmployeeEntity().getPosition()),
                entry("fromCity", travel.getFromCity()),
                entry("toCity", travel.getToCity()),
                entry("startDate", travel.getStartDate().toString()),
                entry("startTime", travel.getStartTime().toString()),
                entry("endDate", travel.getEndDate().toString()),
                entry("endTime", travel.getEndTime().toString()),
//                entry("countBreakfast", String.valueOf(travel.getNumberOfBreakfasts())),
//                entry("countLunch", String.valueOf(travel.getNumberOfLunches())),
//                entry("countDinner", String.valueOf(travel.getNumberOfDinners())),
//                entry("totalAmount", String.valueOf(travel.getTotalAmount())),
//                entry("dietAmount", String.valueOf(travel.getDietAmount())),
//                entry("foodAmount", String.valueOf(travel.getFoodAmount())),
//                entry("overnightStayWithInvoice", String.valueOf(travel.getAmountOfTotalOvernightsStayWithInvoice())),
//                entry("overnightStayWithoutInvoice", String.valueOf(travel.getAmountOfTotalOvernightsStayWithoutInvoice())),
                entry("advancePayment", String.valueOf(travel.getAdvancePayment()))
        );

        String templatePath = "src/main/resources/templates/files/template.pdf";
        String outputPath = "src/main/resources/templates/files/changed_template.pdf";

        fillTemplate(templatePath, outputPath, replacements);
    }

    // method without validation
    private void fillTemplate(String templatePath, String outputPath, Map<String, String> data) throws IOException {
        try (PDDocument document = PDDocument.load(new FileInputStream(templatePath))) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    PDField field = acroForm.getField(entry.getKey());
                    if (field != null) {
                        field.setValue(entry.getValue());
                    }
                }
                acroForm.flatten();
            }
            document.save(outputPath);
        }
    }

    // method with validation
//    private void fillTemplate(String templatePath, String outputPath, Map<String, String> data) throws IOException {
//        try (PDDocument document = PDDocument.load(new FileInputStream(templatePath))) {
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//
//            if (acroForm != null) {
//                for (PDField field : acroForm.getFields()) {
//                    System.out.println("Field name: " + field.getFullyQualifiedName());
//                }
//
//                for (Map.Entry<String, String> entry : data.entrySet()) {
//                    PDField field = acroForm.getField(entry.getKey());
//                    if (field != null) {
//                        field.setValue(entry.getValue());
//                        System.out.println("Setting field " + entry.getKey() + " to " + entry.getValue());
//                    } else {
//                        System.out.println("Field " + entry.getKey() + " not found in the form.");
//                    }
//                }
//                acroForm.flatten();
//            } else {
//                System.out.println("AcroForm is null.");
//            }
//            document.save(outputPath);
//        }
//    }
}
