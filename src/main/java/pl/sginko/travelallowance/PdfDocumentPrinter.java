package pl.sginko.travelallowance;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.sginko.travelallowance.model.Entity.TravelEntity;
import pl.sginko.travelallowance.repository.TravelRepository;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class PdfDocumentPrinter {
    private final TravelRepository travelRepository;

    public PdfDocumentPrinter(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }


    public void generatePdfDocument(Long id) throws IOException {
        TravelEntity businessTrip = travelRepository.findById(id).orElseThrow(() -> new RuntimeException("Business trip not found"));

        Map<String, String> replacements = Map.of(
                "startDate", businessTrip.getStartTravel().toString(),
                "endDate", businessTrip.getFinishTravel().toString(),
                "countBreakfast", String.valueOf(businessTrip.getBreakfastQuantity()),
                "countLunch", String.valueOf(businessTrip.getLunchQuantity()),
                "countDinner", String.valueOf(businessTrip.getDinnerQuantity()),
                "totalAmount", String.valueOf(businessTrip.getTotalAmount()),
                "dietAmount", String.valueOf(businessTrip.getDietAmount()),
                "foodAmount", String.valueOf(businessTrip.getFoodAmount())
        );

        String templatePath = "src/main/resources/templates/files/template.pdf";
        String outputPath = "src/main/resources/templates/files/changed_template.pdf";

        fillTemplate(templatePath, outputPath, replacements);
    }

//    private void fillTemplate(String templatePath, String outputPath, Map<String, String> data) throws IOException {
//        try (PDDocument document = PDDocument.load(new FileInputStream(templatePath))) {
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//
//            if (acroForm != null) {
//                for (Map.Entry<String, String> entry : data.entrySet()) {
//                    PDField field = acroForm.getField(entry.getKey());
//                    if (field != null) {
//                        field.setValue(entry.getValue());
//                    }
//                }
//                acroForm.flatten();
//            }
//
//            document.save(outputPath);
//        }
//    }

    private void fillTemplate(String templatePath, String outputPath, Map<String, String> data) throws IOException {
        try (PDDocument document = PDDocument.load(new FileInputStream(templatePath))) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                for (PDField field : acroForm.getFields()) {
                    System.out.println("Field name: " + field.getFullyQualifiedName());
                }

                for (Map.Entry<String, String> entry : data.entrySet()) {
                    PDField field = acroForm.getField(entry.getKey());
                    if (field != null) {
                        field.setValue(entry.getValue());
                        System.out.println("Setting field " + entry.getKey() + " to " + entry.getValue());
                    } else {
                        System.out.println("Field " + entry.getKey() + " not found in the form.");
                    }
                }
                acroForm.flatten();
            } else {
                System.out.println("AcroForm is null.");
            }

            document.save(outputPath);
        }
    }



//    public static void fillTemplate(String templatePath, String outputPath, Map<String, String> data) throws IOException {
//        try (PDDocument document = PDDocument.load(new FileInputStream(templatePath))) {
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//
//            if (acroForm != null) {
//                for (Map.Entry<String, String> entry : data.entrySet()) {
//                    PDField field = acroForm.getField(entry.getKey());
//                    if (field != null) {
//                        field.setValue(entry.getValue());
//                    }
//                }
//                acroForm.flatten();
//            }
//
//            document.save(outputPath);
//        }
//    }
//
//    public static void printPdf(String filePath) throws PrintException, IOException {
//        FileInputStream fis = new FileInputStream(filePath);
//        Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.PDF, null);
//
//        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
//        if (printService != null) {
//            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
//            attributes.add(MediaSizeName.ISO_A4);
//            attributes.add(Sides.ONE_SIDED);
//            DocPrintJob printJob = printService.createPrintJob();
//            printJob.print(pdfDoc, attributes);
//        } else {
//            System.out.println("No print service found.");
//        }
//        fis.close();
//    }
}
