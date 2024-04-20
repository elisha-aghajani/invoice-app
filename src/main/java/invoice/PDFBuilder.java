package invoice;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.commons.text.WordUtils;
import utils.Constants;

import java.io.FileNotFoundException;

public class PDFBuilder {

    private final Invoice invoice;
    private final String path;
    Document doc;

    public PDFBuilder(Invoice invoice, String path) {
        this.invoice = invoice;
        this.path = path;
    }

    public boolean openPDF() {
        try {
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            pdfDoc.addNewPage();
            this.doc = new Document(pdfDoc);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void generatePDF() {
        doc.add(getHeaderTable());
        doc.add(getLineSpacing());
        doc.add(getCustomerTable());
        doc.add(getLineSpacing());
        doc.add(getInvoiceTable());
        doc.add(getLineSpacing());
        doc.add(getPaymentTable());
        doc.close();
    }

    private Table getHeaderTable() {
        Paragraph paragraph;
        Cell cell;

        float[] headerTableColWidth = {280f, 280f};
        Table headerTable = new Table(headerTableColWidth);
        headerTable.setBackgroundColor(new DeviceRgb(65, 170, 220));
        headerTable.setFontColor(new DeviceRgb(255, 255, 255));

        paragraph = new Paragraph("Invoice");
        cell = new Cell();
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setMarginTop(30f);
        cell.setMarginBottom(30f);
        cell.setFontSize(30f);
        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);
        headerTable.addCell(cell);

        paragraph = new Paragraph(String.format("%s%n%s%n%s, %s %s",
                WordUtils.capitalizeFully(invoice.getCompany().getName()),
                WordUtils.capitalizeFully(invoice.getCompany().getAddress().getStreet()), WordUtils.capitalizeFully(invoice.getCompany().getAddress().getCity()),
                Constants.STATES_ABRV.get(invoice.getCompany().getAddress().getState()),
                invoice.getCompany().getAddress().getZipcode()));
        cell = new Cell();
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setMarginTop(30f);
        cell.setMarginBottom(30f);
        cell.setMarginRight(50f);
        cell.setFontSize(12f);
        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);
        headerTable.addCell(cell);

        return headerTable;
    }

    private Table getCustomerTable() {
        Paragraph paragraph;
        Cell cell;

        float[] customerTableColWidth = {80, 300};
        Table customerTable = new Table(customerTableColWidth);

        paragraph = new Paragraph("Customer Information");
        cell = new Cell((0), (2));
        cell.setBold();
        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);
        customerTable.addCell(cell);

        paragraph = new Paragraph("Name: ");
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);
        customerTable.addCell(cell);

        paragraph = new Paragraph(WordUtils.capitalizeFully(invoice.getCustomer().getName()));
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);
        customerTable.addCell(cell);

        paragraph = new Paragraph("Address: ");
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);
        customerTable.addCell(cell);

        paragraph = new Paragraph(String.format("%s, %s, %s %s",
                WordUtils.capitalizeFully(invoice.getCustomer().getAddress().getStreet()),
                WordUtils.capitalizeFully(invoice.getCustomer().getAddress().getCity()),
                Constants.STATES_ABRV.get(invoice.getCompany().getAddress().getState()),
                invoice.getCustomer().getAddress().getZipcode()));
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);
        customerTable.addCell(cell);

        return customerTable;
    }

    private Table getInvoiceTable() {
        Paragraph paragraph;
        Cell cell;

        float[] invoiceTableColWidth = {420, 140};
        Table invoiceTable = new Table(invoiceTableColWidth);

        paragraph = new Paragraph("Description");
        cell = new Cell();
        cell.setBackgroundColor(new DeviceRgb(65, 170, 220));
        cell.setFontColor(new DeviceRgb(255, 255, 255));
        cell.add(paragraph);
        invoiceTable.addCell(cell);

        paragraph = new Paragraph("Price");
        cell = new Cell();
        cell.setBackgroundColor(new DeviceRgb(65, 170, 220));
        cell.setFontColor(new DeviceRgb(255, 255, 255));
        cell.add(paragraph);
        invoiceTable.addCell(cell);

        for (int i = 0; i < invoice.getInvoiceRows().size(); i++) {
            paragraph = new Paragraph(formatString(invoice.getInvoiceRows().get(i).getDescription()));
            cell = new Cell();
            cell.add(paragraph);
            invoiceTable.addCell(cell);

            paragraph = new Paragraph(invoice.getInvoiceRows().get(i).getPrice() == 0 ? "" : String.valueOf(invoice.getInvoiceRows().get(i).getPrice()));
            cell = new Cell();
            cell.add(paragraph);
            invoiceTable.addCell(cell);
        }

        return invoiceTable;
    }

    private Table getPaymentTable() {
        Paragraph paragraph;
        Cell cell;

        float[] paymentTableColWidth = {65, 65};
        Table paymentTable = new Table(paymentTableColWidth);
        paymentTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        paymentTable.setBackgroundColor(new DeviceRgb(65, 170, 220));

        paragraph = new Paragraph("Subtotal");
        cell = new Cell();
        cell.setFontColor(new DeviceRgb(255, 255, 255));
        cell.add(paragraph);
        paymentTable.addCell(cell);

        paragraph = new Paragraph(String.valueOf(invoice.getPayment().getSubTotal()));
        cell = new Cell();
        cell.setFontColor(new DeviceRgb(255, 255, 255));
        cell.add(paragraph);
        paymentTable.addCell(cell);

        paragraph = new Paragraph("Paid");
        cell = new Cell();
        cell.setFontColor(new DeviceRgb(255, 255, 255));
        cell.add(paragraph);
        paymentTable.addCell(cell);

        paragraph = new Paragraph(String.valueOf(invoice.getPayment().getPaid()));
        cell = new Cell();
        cell.setFontColor(new DeviceRgb(255, 255, 255));
        cell.add(paragraph);
        paymentTable.addCell(cell);

        paragraph = new Paragraph("Total");
        cell = new Cell();
        cell.setFontColor(new DeviceRgb(255, 255, 255));
        cell.add(paragraph);
        paymentTable.addCell(cell);

        paragraph = new Paragraph(String.valueOf(invoice.getPayment().getTotal()));
        cell = new Cell();
        cell.setFontColor(new DeviceRgb(255, 255, 255));
        cell.add(paragraph);
        paymentTable.addCell(cell);

        return paymentTable;
    }

    private Paragraph getLineSpacing() {
        return new Paragraph(String.format("%n"));
    }

    private String formatString(String str) {
        StringBuilder newStr = new StringBuilder();
        int charsOnCurrentLine = 0;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i)) && charsOnCurrentLine >= 60) {
                newStr.append(System.lineSeparator());
                charsOnCurrentLine = 0;
            }
            newStr.append(str.charAt(i));
            charsOnCurrentLine++;
        }
        return newStr.toString();
    }
}
