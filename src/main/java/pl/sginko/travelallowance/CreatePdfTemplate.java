//package pl.sginko.travelallowance;
//
//
//import java.io.File;
//import java.io.IOException;
//
//public class CreatePdfTemplate {
//        public static void main(String[] args) throws IOException {
//            // Path to the PDF template
//            String templatePath = "src/main/resources/templates/template.pdf";
//            File file = new File(templatePath);
//            file.getParentFile().mkdirs();
//
//            // Create a new PDF document
//            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(templatePath));
//            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
//
//            // Create form fields
//            PdfFormField startDateField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(100, 750, 200, 20), "startDate", "");
//            PdfFormField endDateField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(100, 720, 200, 20), "endDate", "");
//            PdfFormField countBreakfastField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(100, 690, 200, 20), "countBreakfast", "");
//            PdfFormField countLunchField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(100, 660, 200, 20), "countLunch", "");
//            PdfFormField countDinnerField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(100, 630, 200, 20), "countDinner", "");
//            PdfFormField totalAmountField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(100, 600, 200, 20), "totalAmount", "");
//            PdfFormField dietAmountField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(100, 570, 200, 20), "dietAmount", "");
//            PdfFormField foodAmountField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(100, 540, 200, 20), "foodAmount", "");
//
//            // Add fields to the form
//            form.addField(startDateField);
//            form.addField(endDateField);
//            form.addField(countBreakfastField);
//            form.addField(countLunchField);
//            form.addField(countDinnerField);
//            form.addField(totalAmountField);
//            form.addField(dietAmountField);
//            form.addField(foodAmountField);
//
//            // Close the document
//            pdfDoc.close();
//        }
//    }
//
//}
