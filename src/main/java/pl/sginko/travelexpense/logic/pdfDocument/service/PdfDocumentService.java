package pl.sginko.travelexpense.logic.pdfDocument.service;

import java.io.IOException;
import java.util.UUID;

public interface PdfDocumentService {

    void generateTravelExpenseReportPdf(UUID techId) throws IOException;
}