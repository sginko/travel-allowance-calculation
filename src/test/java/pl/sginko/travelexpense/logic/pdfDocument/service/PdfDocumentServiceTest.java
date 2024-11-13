package pl.sginko.travelexpense.logic.pdfDocument.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.logic.travelexpense.diet.entity.DietEntity;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;
import pl.sginko.travelexpense.logic.travelexpense.transportCost.entity.TransportCostEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.entity.TravelEntity;
import pl.sginko.travelexpense.logic.travelexpense.travel.exception.TravelException;
import pl.sginko.travelexpense.logic.travelexpense.travel.repository.TravelRepository;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;

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
    private TravelRepository travelRepository;

    @InjectMocks
    private PdfDocumentServiceImpl pdfDocumentService;

    @Mock
    private TravelEntity travelEntity;

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

        when(travelEntity.getTechId()).thenReturn(techId);
        when(travelEntity.getUserEntity()).thenReturn(userEntity);
        when(travelEntity.getFromCity()).thenReturn("CityA");
        when(travelEntity.getToCity()).thenReturn("CityB");
        when(travelEntity.getStartDate()).thenReturn(LocalDate.now());
        when(travelEntity.getStartTime()).thenReturn(LocalTime.of(8, 0));
        when(travelEntity.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(travelEntity.getEndTime()).thenReturn(LocalTime.of(18, 0));
        when(travelEntity.getDietEntity()).thenReturn(dietEntity);
        when(travelEntity.getOvernightStayEntity()).thenReturn(overnightStayEntity);
        when(travelEntity.getTransportCostEntity()).thenReturn(transportCostEntity);
        when(travelEntity.getAdvancePayment()).thenReturn(BigDecimal.valueOf(500));
        when(travelEntity.getOtherExpenses()).thenReturn(BigDecimal.valueOf(200));
        when(travelEntity.getTotalAmount()).thenReturn(BigDecimal.valueOf(1000));

        when(travelRepository.findByTechId(techId)).thenReturn(Optional.of(travelEntity));
    }

    @Test
    void should_generate_pdf_document() throws IOException {
        // WHEN
        pdfDocumentService.generateTravelExpenseReportPdf(techId);

        // THEN
        File outputFile = new File("src/main/resources/print/changed_template.pdf");
        assertTrue(outputFile.exists());
        assertTrue(outputFile.delete());
    }

    @Test
    void should_throw_exception_when_travel_not_found() {
        // GIVEN
        UUID invalidTechId = UUID.randomUUID();
        when(travelRepository.findByTechId(invalidTechId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(TravelException.class, () -> pdfDocumentService.generateTravelExpenseReportPdf(invalidTechId));
    }

//    @Test
//    void shouldThrowPdfDocumentExceptionWhenTemplatePathIsInvalid() {
//        // GIVEN
//        UUID techId = UUID.randomUUID();
//        String invalidTemplatePath = "src/main/resources/print/non_existing_template.pdf"; // Неверный путь к шаблону
//        when(travelRepository.findByTechId(techId)).thenReturn(Optional.of(travelEntity));
//
//        // WHEN & THEN
//        assertThrows(PdfDocumentException.class, () -> {
//            pdfDocumentService.generatePdfDocument(techId);
//        });
//    }
}
