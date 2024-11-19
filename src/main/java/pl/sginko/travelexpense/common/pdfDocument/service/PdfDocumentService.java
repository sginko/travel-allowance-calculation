package pl.sginko.travelexpense.common.pdfDocument.service;

import java.io.IOException;
import java.util.UUID;

public interface PdfDocumentService {

    void generateTravelExpenseReportPdf(UUID techId) throws IOException;

//    ByteArrayOutputStream generateTravelExpenseReportPdfAsStream(UUID techId);
}
