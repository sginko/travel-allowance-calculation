package pl.sginko.travelexpense.common.pdfDocument.exception;

import java.io.IOException;

public class PdfDocumentException extends IOException {

    public PdfDocumentException(String message) {
        super(message);
    }
}
