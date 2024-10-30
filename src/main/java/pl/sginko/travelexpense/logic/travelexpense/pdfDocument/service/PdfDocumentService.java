package pl.sginko.travelexpense.logic.travelexpense.pdfDocument.service;

import com.ibm.icu.text.RuleBasedNumberFormat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.transport.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

import static java.util.Map.entry;

@Service
public class PdfDocumentService {
    private final TravelRepository travelRepository;

    public PdfDocumentService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public void generatePdfDocument(Long id) throws IOException {
        TravelEntity travelEntity = travelRepository.findById(id).orElseThrow(() -> new TravelException("Travel not found"));
        DietEntity dietEntity = travelEntity.getDietEntity();
        OvernightStayEntity overnightStayEntity = travelEntity.getOvernightStayEntity();
        TransportCostEntity transportCostEntity = travelEntity.getTransportCostEntity();

        Map<String, String> replacements = Map.ofEntries(
                entry("fullName", travelEntity.getUserEntity().getName() + " " + travelEntity.getUserEntity().getSurname()),
//                entry("position", travelEntity.getUserEntity().getPosition()),
                entry("fromCity", travelEntity.getFromCity()),
                entry("toCity", travelEntity.getToCity()),
                entry("startDate", travelEntity.getStartDate().toString()),
                entry("startTime", travelEntity.getStartTime().toString()),
                entry("endDate", travelEntity.getEndDate().toString()),
                entry("endTime", travelEntity.getEndTime().toString()),
                entry("countBreakfast", String.valueOf(dietEntity.getNumberOfBreakfasts())),
                entry("countLunch", String.valueOf(dietEntity.getNumberOfLunches())),
                entry("countDinner", String.valueOf(dietEntity.getNumberOfDinners())),
                entry("totalAmount", String.valueOf(travelEntity.getTotalAmount())),
                entry("totalAmountWords", numberToWords(travelEntity.getTotalAmount())),
                entry("dietAmount", String.valueOf(dietEntity.getDietAmount())),
                entry("foodAmount", String.valueOf(dietEntity.getFoodAmount())),
                entry("overnightStayWithInvoice", String.valueOf(overnightStayEntity.getAmountOfTotalOvernightsStayWithInvoice())),
                entry("overnightStayWithoutInvoice", String.valueOf(overnightStayEntity.getAmountOfTotalOvernightsStayWithoutInvoice())),
                entry("advancePayment", String.valueOf(travelEntity.getAdvancePayment())),
                entry("advancePaymentWords", numberToWords(travelEntity.getAdvancePayment())),
                entry("undocumentedLocalTransportCost", String.valueOf(transportCostEntity.getUndocumentedLocalTransportCost())),
                entry("documentedLocalTransportCost", String.valueOf(transportCostEntity.getDocumentedLocalTransportCost())),
                entry("meansOfTransport", String.valueOf(transportCostEntity.getMeansOfTransport())),
                entry("totalCostOfTravelByOwnAndPublicTransport", String.valueOf(transportCostEntity.getTotalCostOfTravelByOwnAndPublicTransport())),
                entry("transportCostAmount", String.valueOf(transportCostEntity.getTransportCostAmount())),
                entry("otherExpenses", String.valueOf(travelEntity.getOtherExpenses()))
        );

        //without Docker
        String templatePath = "src/main/resources/print/template.pdf";
        String outputPath = "src/main/resources/print/changed_template.pdf";

        //with Docker
//        String templatePath = "/app/resources/print/template.pdf";
//        String outputPath = "/app/resources/print/changed_template.pdf";

        fillTemplate(templatePath, outputPath, replacements);
    }

    private String numberToWords(BigDecimal number) {
        RuleBasedNumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(Locale.forLanguageTag("pl-PL"), RuleBasedNumberFormat.SPELLOUT);
        return ruleBasedNumberFormat.format(number);
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
