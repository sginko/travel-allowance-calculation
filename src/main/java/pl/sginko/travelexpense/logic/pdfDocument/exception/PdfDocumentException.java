package pl.sginko.travelexpense.logic.pdfDocument.exception;

import java.io.IOException;

public class PdfDocumentException extends IOException {

    public PdfDocumentException(String message) {
        super(message);
    }
}
