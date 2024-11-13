package pl.sginko.travelexpense.logic.pdfDocument.service;

import com.ibm.icu.text.RuleBasedNumberFormat;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.pdfDocument.exception.PdfDocumentException;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.logic.travelexpense.travelReport.repository.TravelReportRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;

@AllArgsConstructor
@Service
public class PdfDocumentServiceImpl implements PdfDocumentService {
    private final TravelReportRepository travelReportRepository;

    @Override
    public void generateTravelExpenseReportPdf(UUID techId) throws IOException {
        TravelReportEntity travelReportEntity = travelReportRepository.findByTechId(techId)
                .orElseThrow(() -> new TravelReportException("Travel not found"));
        DietEntity dietEntity = travelReportEntity.getDietEntity();
        OvernightStayEntity overnightStayEntity = travelReportEntity.getOvernightStayEntity();
        TransportCostEntity transportCostEntity = travelReportEntity.getTransportCostEntity();

        Map<String, String> replacements = Map.ofEntries(
                entry("fullName", travelReportEntity.getUserEntity().getName() + " " + travelReportEntity.getUserEntity().getSurname()),
//                entry("position", travelEntity.getUserEntity().getPosition()),
                entry("fromCity", travelReportEntity.getFromCity()),
                entry("toCity", travelReportEntity.getToCity()),
                entry("startDate", travelReportEntity.getStartDate().toString()),
                entry("startTime", travelReportEntity.getStartTime().toString()),
                entry("endDate", travelReportEntity.getEndDate().toString()),
                entry("endTime", travelReportEntity.getEndTime().toString()),
                entry("countBreakfast", String.valueOf(dietEntity.getNumberOfBreakfasts())),
                entry("countLunch", String.valueOf(dietEntity.getNumberOfLunches())),
                entry("countDinner", String.valueOf(dietEntity.getNumberOfDinners())),
                entry("totalAmount", String.valueOf(travelReportEntity.getTotalAmount())),
                entry("totalAmountWords", numberToWords(travelReportEntity.getTotalAmount())),
                entry("dietAmount", String.valueOf(dietEntity.getDietAmount())),
                entry("foodAmount", String.valueOf(dietEntity.getFoodAmount())),
                entry("overnightStayWithInvoice", String.valueOf(overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice())),
                entry("overnightStayWithoutInvoice", String.valueOf(overnightStayEntity.getTotalAmountOfOvernightsStayWithoutInvoice())),
                entry("advancePayment", String.valueOf(travelReportEntity.getAdvancePayment())),
                entry("advancePaymentWords", numberToWords(travelReportEntity.getAdvancePayment())),
                entry("undocumentedLocalTransportCost", String.valueOf(transportCostEntity.getUndocumentedLocalTransportCost())),
                entry("documentedLocalTransportCost", String.valueOf(transportCostEntity.getDocumentedLocalTransportCost())),
                entry("meansOfTransport", String.valueOf(transportCostEntity.getMeansOfTransport())),
                entry("totalCostOfTravelByOwnAndPublicTransport", String.valueOf(transportCostEntity.getTotalCostOfTravelByOwnAndPublicTransport())),
                entry("transportCostAmount", String.valueOf(transportCostEntity.getTransportCostAmount())),
                entry("otherExpenses", String.valueOf(travelReportEntity.getOtherExpenses())));

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
        } catch (IOException e) {
            throw new PdfDocumentException("Error processing PDF template: " + e.getMessage());
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
