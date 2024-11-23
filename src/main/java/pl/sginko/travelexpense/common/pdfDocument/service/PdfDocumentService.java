package pl.sginko.travelexpense.common.pdfDocument.service;

import pl.sginko.travelexpense.common.pdfDocument.exception.PdfDocumentException;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public interface PdfDocumentService {

    ByteArrayOutputStream generateTravelExpenseReportPdfAsStream(UUID techId) throws PdfDocumentException;
}
