package pl.sginko.travelexpense.logic.travelexpense.pdfDocument.exception;

import java.io.IOException;

public class PdfDocumentException extends IOException {

    public PdfDocumentException(String message) {
        super(message);
    }
}
