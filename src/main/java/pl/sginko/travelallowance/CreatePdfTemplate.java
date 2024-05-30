//package pl.sginko.travelallowance;
//
//import com.itextpdf.forms.PdfAcroForm;
//import com.itextpdf.forms.fields.PdfFormField;
//import com.itextpdf.kernel.geom.PageSize;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//
//import java.io.File;
//import java.io.IOException;
//
//public class CreatePdfTemplate {
//    public static void main(String[] args) throws IOException {
//        // Path to the PDF template
//        String templatePath = "src/main/resources/templates/template.pdf";
//        File file = new File(templatePath);
//        file.getParentFile().mkdirs();
//
//        // Create a new PDF document
//        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(templatePath));
//        Document document = new Document(pdfDoc, PageSize.A4);
//
//        // Add text and fields to the document
//        document.add(new Paragraph("Wyniki"));
//        document.add(new Paragraph("Data i godzina wyjazdu:"));
//        PdfFormField startDateField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(150, 770, 200, 20), "startDate", "");
//        document.add(new Paragraph("Data i godzina powrotu:"));
//        PdfFormField endDateField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(150, 740, 200, 20), "endDate", "");
//        document.add(new Paragraph("Liczba śniadań:"));
//        PdfFormField countBreakfastField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(150, 710, 200, 20), "countBreakfast", "");
//        document.add(new Paragraph("Liczba obiadów:"));
//        PdfFormField countLunchField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(150, 680, 200, 20), "countLunch", "");
//        document.add(new Paragraph("Liczba kolacji:"));
//        PdfFormField countDinnerField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(150, 650, 200, 20), "countDinner", "");
//        document.add(new Paragraph("Do wypłaty:"));
//        PdfFormField totalAmountField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(150, 620, 200, 20), "totalAmount", "");
//        document.add(new Paragraph("Dieta:"));
//        PdfFormField dietAmountField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(150, 590, 200, 20), "dietAmount", "");
//        document.add(new Paragraph("Posiłki:"));
//        PdfFormField foodAmountField = PdfFormField.createText(pdfDoc, new com.itextpdf.kernel.geom.Rectangle(150, 560, 200, 20), "foodAmount", "");
//
//        // Add fields to the form
//        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
//        form.addField(startDateField);
//        form.addField(endDateField);
//        form.addField(countBreakfastField);
//        form.addField(countLunchField);
//        form.addField(countDinnerField);
//        form.addField(totalAmountField);
//        form.addField(dietAmountField);
//        form.addField(foodAmountField);
//
//        // Close the document
//        document.close();
//    }
//}
