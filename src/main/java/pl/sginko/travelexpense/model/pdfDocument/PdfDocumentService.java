package pl.sginko.travelexpense.model.pdfDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.diet.DietException;
import pl.sginko.travelexpense.model.diet.entity.DietEntity;
import pl.sginko.travelexpense.model.diet.repository.DietRepository;
import pl.sginko.travelexpense.model.overnightStay.OvernightStayException;
import pl.sginko.travelexpense.model.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.model.overnightStay.repository.OvernightStayRepository;
import pl.sginko.travelexpense.model.travel.TravelException;
import pl.sginko.travelexpense.model.travel.entity.TravelEntity;
import pl.sginko.travelexpense.model.travel.repository.TravelRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static java.util.Map.entry;

@Service
public class PdfDocumentService {
    private final TravelRepository travelRepository;
    private final DietRepository dietRepository;
    private final OvernightStayRepository overnightStayRepository;

    public PdfDocumentService(TravelRepository travelRepository, DietRepository dietRepository, OvernightStayRepository overnightStayRepository) {
        this.travelRepository = travelRepository;
        this.dietRepository = dietRepository;
        this.overnightStayRepository = overnightStayRepository;
    }

    public void generatePdfDocument(Long id) throws IOException {
        TravelEntity travelFound = travelRepository.findById(id).orElseThrow(() -> new TravelException("Travel not found"));
        DietEntity dietFound = dietRepository.findById(id).orElseThrow(() -> new DietException("Diet not found"));
        OvernightStayEntity overnightStayFound = overnightStayRepository.findById(id).orElseThrow(() -> new OvernightStayException("Overnight Stay not found"));

        Map<String, String> replacements = Map.ofEntries(
                entry("fullName", travelFound.getEmployeeEntity().getFirstName() + " " + travelFound.getEmployeeEntity().getSecondName()),
                entry("position", travelFound.getEmployeeEntity().getPosition()),
                entry("fromCity", travelFound.getFromCity()),
                entry("toCity", travelFound.getToCity()),
                entry("startDate", travelFound.getStartDate().toString()),
                entry("startTime", travelFound.getStartTime().toString()),
                entry("endDate", travelFound.getEndDate().toString()),
                entry("endTime", travelFound.getEndTime().toString()),
                entry("countBreakfast", String.valueOf(dietFound.getNumberOfBreakfasts())),
                entry("countLunch", String.valueOf(dietFound.getNumberOfLunches())),
                entry("countDinner", String.valueOf(dietFound.getNumberOfDinners())),
                entry("totalAmount", String.valueOf(travelFound.getTotalAmount())),
                entry("dietAmount", String.valueOf(dietFound.getDietAmount())),
//                entry("foodAmount", String.valueOf(dietFound.getFoodAmount())),
                entry("overnightStayWithInvoice", String.valueOf(overnightStayFound.getAmountOfTotalOvernightsStayWithInvoice())),
                entry("overnightStayWithoutInvoice", String.valueOf(overnightStayFound.getAmountOfTotalOvernightsStayWithoutInvoice())),
                entry("advancePayment", String.valueOf(travelFound.getAdvancePayment()))
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
