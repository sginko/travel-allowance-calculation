package pl.sginko.travelexpense.common.pdfDocument.service;

import com.ibm.icu.text.RuleBasedNumberFormat;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.common.pdfDocument.exception.PdfDocumentException;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.repository.TravelReportRepository;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    public ByteArrayOutputStream generateTravelExpenseReportPdfAsStream(UUID techId) throws PdfDocumentException {
        TravelReportEntity travelReportEntity = travelReportRepository.findByTechId(techId)
                .orElseThrow(() -> new TravelReportException("Travel not found"));

        Map<String, String> replacements = prepareReplacements(travelReportEntity);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            fillTemplate("src/main/resources/print/template.pdf", outputStream, replacements);   //without Docker
//            fillTemplate("/app/resources/print/template.pdf", outputStream, replacements);   // with Docker
            return outputStream;
        } catch (IOException e) {
            throw new PdfDocumentException("Error processing PDF template: " + e.getMessage());
        }
    }

    private Map<String, String> prepareReplacements(TravelReportEntity travelReportEntity) {
        DietEntity dietEntity = travelReportEntity.getDietEntity();
        OvernightStayEntity overnightStayEntity = travelReportEntity.getOvernightStayEntity();
        TransportCostEntity transportCostEntity = travelReportEntity.getTransportCostEntity();

        return Map.ofEntries(
                entry("fullName", travelReportEntity.getUserEntity().getName() + " " + travelReportEntity.getUserEntity().getSurname()),
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
                entry("otherExpenses", String.valueOf(travelReportEntity.getOtherExpenses()))
        );
    }

    private void fillTemplate(String templatePath, OutputStream outputStream, Map<String, String> data) throws IOException {
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

            document.save(outputStream);
        }
    }

    private String numberToWords(BigDecimal number) {
        RuleBasedNumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(Locale.forLanguageTag("pl-PL"), RuleBasedNumberFormat.SPELLOUT);
        return ruleBasedNumberFormat.format(number);
    }
}
