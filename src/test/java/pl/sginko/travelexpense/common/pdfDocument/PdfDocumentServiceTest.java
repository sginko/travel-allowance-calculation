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
package pl.sginko.travelexpense.common.pdfDocument;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sginko.travelexpense.common.pdfDocument.exception.PdfDocumentException;
import pl.sginko.travelexpense.common.pdfDocument.service.PdfDocumentServiceImpl;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponsePdfDto;
import pl.sginko.travelexpense.domain.travelReport.entity.DietEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TransportCostEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.mapper.TravelReportMapper;
import pl.sginko.travelexpense.domain.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PdfDocumentServiceTest {
    @Mock
    private TravelReportRepository travelReportRepository;

    @Mock
    private TravelReportMapper travelReportMapper;

    @InjectMocks
    private PdfDocumentServiceImpl pdfDocumentService;

    private UUID techId;
    private TravelReportEntity travelReportEntity;
    private TravelReportResponsePdfDto responsePdfDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        techId = UUID.randomUUID();

        travelReportEntity = new TravelReportEntity("CityA", "CityB", LocalDate.now(), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                new UserEntity("email@test.com", "Name", "Surname", "password"),
                BigDecimal.valueOf(500), BigDecimal.valueOf(200));

        travelReportEntity.setDietDetails(new DietEntity(travelReportEntity, BigDecimal.valueOf(45), 1,
                1, 1));

        travelReportEntity.setOvernightStayDetails(new OvernightStayEntity(travelReportEntity, 1,
                0, BigDecimal.ZERO, false));

        travelReportEntity.setTransportCostDetails(new TransportCostEntity(travelReportEntity, 0,
                BigDecimal.ZERO, "Public", BigDecimal.valueOf(100), 0L,
                0L, 0L, 0L));

        travelReportEntity.calculateTotalAmount();

        responsePdfDto = new TravelReportResponsePdfDto("Name", "Surname", "CityA", "CityB",
                LocalDate.now(), LocalTime.of(8, 0), LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                1, 1, 1, BigDecimal.valueOf(1000), BigDecimal.valueOf(45),
                BigDecimal.valueOf(25), BigDecimal.valueOf(200), BigDecimal.valueOf(100), BigDecimal.valueOf(500),
                BigDecimal.valueOf(0), BigDecimal.valueOf(0), "Public", BigDecimal.valueOf(100),
                BigDecimal.valueOf(200), BigDecimal.valueOf(200));

        when(travelReportRepository.findByTechId(techId)).thenReturn(Optional.of(travelReportEntity));
        when(travelReportMapper.toResponsePdfDto(travelReportEntity)).thenReturn(responsePdfDto);
    }

//    @Test
//    void should_generate_pdf_document_successfully() throws PdfDocumentException {
//        // WHEN
//        var pdfStream = pdfDocumentService.generateTravelExpenseReportPdfAsStream(techId);
//
//        // THEN
//        assertNotNull(pdfStream, "PDF stream should not be null");
//        verify(travelReportRepository, times(1)).findByTechId(techId);
//        verify(travelReportMapper, times(1)).toResponsePdfDto(travelReportEntity);
//    }

    @Test
    void should_throw_exception_when_travel_not_found() {
        // GIVEN
        UUID invalidTechId = UUID.randomUUID();
        when(travelReportRepository.findByTechId(invalidTechId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(TravelReportException.class,
                () -> pdfDocumentService.generateTravelExpenseReportPdfAsStream(invalidTechId));
        verify(travelReportRepository, times(1)).findByTechId(invalidTechId);
        verifyNoInteractions(travelReportMapper);
    }
}
