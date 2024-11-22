package pl.sginko.travelexpense.common.pdfDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.common.pdfDocument.service.PdfDocumentServiceImpl;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PdfDocumentServiceTest {
    @Mock
    private TravelReportRepository travelReportRepository;

    @InjectMocks
    private PdfDocumentServiceImpl pdfDocumentService;

    @Mock
    private TravelReportEntity travelReportEntity;

    @Mock
    private DietEntity dietEntity;

    @Mock
    private OvernightStayEntity overnightStayEntity;

    @Mock
    private TransportCostEntity transportCostEntity;

    @Mock
    private UserEntity userEntity;

    private UUID techId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        techId = UUID.randomUUID();

        when(userEntity.getName()).thenReturn("Name");
        when(userEntity.getSurname()).thenReturn("Surname");

        when(dietEntity.getNumberOfBreakfasts()).thenReturn(1);
        when(dietEntity.getNumberOfLunches()).thenReturn(1);
        when(dietEntity.getNumberOfDinners()).thenReturn(1);
        when(dietEntity.getDietAmount()).thenReturn(BigDecimal.valueOf(100));
        when(dietEntity.getFoodAmount()).thenReturn(BigDecimal.valueOf(50));

        when(overnightStayEntity.getTotalAmountOfOvernightsStayWithInvoice()).thenReturn(BigDecimal.valueOf(200));
        when(overnightStayEntity.getTotalAmountOfOvernightsStayWithoutInvoice()).thenReturn(BigDecimal.valueOf(100));

        when(transportCostEntity.getUndocumentedLocalTransportCost()).thenReturn(BigDecimal.valueOf(30));
        when(transportCostEntity.getDocumentedLocalTransportCost()).thenReturn(BigDecimal.valueOf(70));
        when(transportCostEntity.getMeansOfTransport()).thenReturn("Public");
        when(transportCostEntity.getTotalCostOfTravelByOwnAndPublicTransport()).thenReturn(BigDecimal.valueOf(150));
        when(transportCostEntity.getTransportCostAmount()).thenReturn(BigDecimal.valueOf(100));

        when(travelReportEntity.getTechId()).thenReturn(techId);
        when(travelReportEntity.getUserEntity()).thenReturn(userEntity);
        when(travelReportEntity.getFromCity()).thenReturn("CityA");
        when(travelReportEntity.getToCity()).thenReturn("CityB");
        when(travelReportEntity.getStartDate()).thenReturn(LocalDate.now());
        when(travelReportEntity.getStartTime()).thenReturn(LocalTime.of(8, 0));
        when(travelReportEntity.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelReportEntity.getEndTime()).thenReturn(LocalTime.of(18, 0));
        when(travelReportEntity.getDietEntity()).thenReturn(dietEntity);
        when(travelReportEntity.getOvernightStayEntity()).thenReturn(overnightStayEntity);
        when(travelReportEntity.getTransportCostEntity()).thenReturn(transportCostEntity);
        when(travelReportEntity.getAdvancePayment()).thenReturn(BigDecimal.valueOf(500));
        when(travelReportEntity.getOtherExpenses()).thenReturn(BigDecimal.valueOf(200));
        when(travelReportEntity.getTotalAmount()).thenReturn(BigDecimal.valueOf(1000));

        when(travelReportRepository.findByTechId(techId)).thenReturn(Optional.of(travelReportEntity));
    }

//    @Test
//    void should_generate_pdf_document() throws IOException {
//        // WHEN
//        ByteArrayOutputStream pdfStream = pdfDocumentService.generateTravelExpenseReportPdfAsStream(techId);
//
//        // THEN
//        assertNotNull(pdfStream);
//        assertTrue(pdfStream.size() > 0, "PDF stream should not be empty");
//
//        try (PDDocument document = PDDocument.load(pdfStream.toByteArray())) {
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//            assertNotNull(acroForm);
//
//            assertEquals("Name", acroForm.getField("Name").getValueAsString());
//            assertEquals("Surname", acroForm.getField("Surname").getValueAsString());
//            assertEquals("CityA", acroForm.getField("fromCity").getValueAsString());
//            assertEquals("CityB", acroForm.getField("toCity").getValueAsString());
//        }
//    }
//
//    @Test
//    void should_generate_pdf_document() throws IOException {
//        // WHEN
//        ByteArrayOutputStream pdfStream = pdfDocumentService.generateTravelExpenseReportPdfAsStream(techId);
//
//        // THEN
//        assertNotNull(pdfStream);
//        assertTrue(pdfStream.size() > 0, "PDF stream should not be empty");
//
//        try (PDDocument document = PDDocument.load(pdfStream.toByteArray())) {
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//            assertNotNull(acroForm, "AcroForm should not be null");
//
//            // Отладочный вывод доступных полей
//            System.out.println("Available fields in the PDF template:");
//            acroForm.getFields().forEach(field -> System.out.println(" - " + field.getFullyQualifiedName()));
//
//            // Проверяем наличие и значение конкретного поля 'fullName'
//            PDField fullNameField = acroForm.getField("fullName");
//            assertNotNull(fullNameField, "Field 'fullName' should exist in the PDF template");
//            assertEquals("Name Surname", fullNameField.getValueAsString());
//
//            // Проверяем другие поля, если они есть
//            PDField fromCityField = acroForm.getField("fromCity");
//            assertNotNull(fromCityField, "Field 'fromCity' should exist in the PDF template");
//            assertEquals("CityA", fromCityField.getValueAsString());
//
//            PDField toCityField = acroForm.getField("toCity");
//            assertNotNull(toCityField, "Field 'toCity' should exist in the PDF template");
//            assertEquals("CityB", toCityField.getValueAsString());
//        }
//    }

    @Test
    void should_throw_exception_when_travel_not_found() {
        // GIVEN
        UUID invalidTechId = UUID.randomUUID();
        when(travelReportRepository.findByTechId(invalidTechId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(TravelReportException.class, () -> pdfDocumentService.generateTravelExpenseReportPdfAsStream(invalidTechId));
    }
}
