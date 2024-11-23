/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.common.pdfDocument.service;

import com.ibm.icu.text.RuleBasedNumberFormat;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.common.pdfDocument.exception.PdfDocumentException;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponsePdfDto;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.mapper.TravelReportMapper;
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
    private final TravelReportMapper travelReportMapper;

    @Override
    public ByteArrayOutputStream generateTravelExpenseReportPdfAsStream(UUID techId) throws PdfDocumentException {
        TravelReportResponsePdfDto responsePdfDto = travelReportRepository.findByTechId(techId)
                .map(entity -> travelReportMapper.toResponsePdfDto(entity))
                .orElseThrow(() -> new TravelReportException("Travel not found"));

        Map<String, String> replacements = prepareReplacements(responsePdfDto);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            fillTemplate("src/main/resources/print/template.pdf", outputStream, replacements);   //without Docker
            fillTemplate("/app/resources/print/template.pdf", outputStream, replacements);   // with Docker
            return outputStream;
        } catch (IOException e) {
            throw new PdfDocumentException("Error processing PDF template: " + e.getMessage());
        }
    }

    private Map<String, String> prepareReplacements(TravelReportResponsePdfDto responsePdfDto) {

        return Map.ofEntries(
                entry("fullName", responsePdfDto.getName() + " " + responsePdfDto.getSurname()),
                entry("fromCity", responsePdfDto.getFromCity()),
                entry("toCity", responsePdfDto.getToCity()),
                entry("startDate", responsePdfDto.getStartDate().toString()),
                entry("startTime", responsePdfDto.getStartTime().toString()),
                entry("endDate", responsePdfDto.getEndDate().toString()),
                entry("endTime", responsePdfDto.getEndTime().toString()),
                entry("countBreakfast", String.valueOf(responsePdfDto.getNumberOfBreakfasts())),
                entry("countLunch", String.valueOf(responsePdfDto.getNumberOfLunches())),
                entry("countDinner", String.valueOf(responsePdfDto.getNumberOfDinners())),
                entry("totalAmount", String.valueOf(responsePdfDto.getTotalAmount())),
                entry("totalAmountWords", numberToWords(responsePdfDto.getTotalAmount())),
                entry("dietAmount", String.valueOf(responsePdfDto.getDietAmount())),
                entry("foodAmount", String.valueOf(responsePdfDto.getFoodAmount())),
                entry("overnightStayWithInvoice", String.valueOf(responsePdfDto.getTotalAmountOfOvernightsStayWithInvoice())),
                entry("overnightStayWithoutInvoice", String.valueOf(responsePdfDto.getTotalAmountOfOvernightsStayWithoutInvoice())),
                entry("advancePayment", String.valueOf(responsePdfDto.getAdvancePayment())),
                entry("advancePaymentWords", numberToWords(responsePdfDto.getAdvancePayment())),
                entry("undocumentedLocalTransportCost", String.valueOf(responsePdfDto.getUndocumentedLocalTransportCost())),
                entry("documentedLocalTransportCost", String.valueOf(responsePdfDto.getDocumentedLocalTransportCost())),
                entry("meansOfTransport", String.valueOf(responsePdfDto.getMeansOfTransport())),
                entry("totalCostOfTravelByOwnAndPublicTransport", String.valueOf(responsePdfDto.getTotalCostOfTravelByOwnAndPublicTransport())),
                entry("transportCostAmount", String.valueOf(responsePdfDto.getTransportCostAmount())),
                entry("otherExpenses", String.valueOf(responsePdfDto.getOtherExpenses()))
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
