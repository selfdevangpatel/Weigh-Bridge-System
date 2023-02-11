package print;

import java.awt.print.*;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;

import datetime.DateTime;

public class Receipt {

    static PDType1Font receiptFont = PDType1Font.TIMES_BOLD;

    public static PDDocument getReceiptDocument(ReceiptRecord receiptRecord) {

        PDDocument pdDocument = new PDDocument();

        PDRectangle pageSize = new PDRectangle(728.50F, 290.50F);
        PDPage page = new PDPage(pageSize);
        pdDocument.addPage(page);

        try {

            PDPageContentStream pageContentStream = new PDPageContentStream(pdDocument, pdDocument.getPage(0));

            // Serial No.
            pageContentStream.beginText();
            pageContentStream.setFont(receiptFont, 15);
            pageContentStream.newLineAtOffset(345.0F, 167.40F);
            pageContentStream.showText(String.valueOf(receiptRecord.serialNumber()));
            pageContentStream.endText();
            pageContentStream.moveTo(0, 0);

            // Vehicle No.
            pageContentStream.beginText();
            pageContentStream.setFont(receiptFont, 15);
            pageContentStream.newLineAtOffset(95.0F, 144.50F);
            pageContentStream.showText(receiptRecord.vehicleNumber());
            pageContentStream.endText();
            pageContentStream.moveTo(0, 0);

            // Charge
            pageContentStream.beginText();
            pageContentStream.setFont(receiptFont, 15);
            pageContentStream.newLineAtOffset(390.70F, 52.50F);
            pageContentStream.showText(String.valueOf(receiptRecord.charge()));
            pageContentStream.endText();
            pageContentStream.moveTo(0, 0);

            // Party
            if (receiptRecord.party() != null) {

                pageContentStream.beginText();
                pageContentStream.setFont(receiptFont, 15);
                pageContentStream.newLineAtOffset(21.22F, 167.40F);
                pageContentStream.showText(receiptRecord.party());
                pageContentStream.endText();
                pageContentStream.moveTo(0, 0);
            }

            // Container No.
            if (receiptRecord.containerNumber() != null) {

                pageContentStream.beginText();
                pageContentStream.setFont(receiptFont, 12);
                pageContentStream.newLineAtOffset(225.10F, 73.0F);
                pageContentStream.showText("Container No: " + receiptRecord.containerNumber());
                pageContentStream.endText();
                pageContentStream.moveTo(0, 0);
            }

            // Tare Weight
            if (receiptRecord.tareWeight() != 0) {

                pageContentStream.beginText();
                pageContentStream.setFont(receiptFont, 15);
                pageContentStream.newLineAtOffset(112.0F, 90.70F);
                pageContentStream.showText(String.valueOf(receiptRecord.tareWeight()));
                pageContentStream.endText();
                pageContentStream.moveTo(0, 0);

                pageContentStream.beginText();
                pageContentStream.setFont(receiptFont, 12);
                pageContentStream.newLineAtOffset(271.33F, 93.70F);
                pageContentStream.showText(DateTime.dateToString(receiptRecord.tareDate()) + "    " +
                                           DateTime.timeToString(receiptRecord.tareTime()).toUpperCase());
                pageContentStream.endText();
                pageContentStream.moveTo(0, 0);
            }

            // Gross Weight
            if (receiptRecord.grossWeight() != 0) {

                pageContentStream.beginText();
                pageContentStream.setFont(receiptFont, 15);
                pageContentStream.newLineAtOffset(112.0F, 119.05F);
                pageContentStream.showText(String.valueOf(receiptRecord.grossWeight()));
                pageContentStream.endText();
                pageContentStream.moveTo(0, 0);

                pageContentStream.beginText();
                pageContentStream.setFont(receiptFont, 12);
                pageContentStream.newLineAtOffset(271.33F, 121.05F);
                pageContentStream.showText(DateTime.dateToString(receiptRecord.grossDate()) + "    " +
                                           DateTime.timeToString(receiptRecord.grossTime()).toUpperCase());
                pageContentStream.endText();
                pageContentStream.moveTo(0, 0);
            }

            // Net Weight
            if (receiptRecord.netWeight() != 0) {

                pageContentStream.beginText();
                pageContentStream.setFont(receiptFont, 15);
                pageContentStream.newLineAtOffset(112.0F, 62.35F);
                pageContentStream.showText(String.valueOf(receiptRecord.netWeight()));
                pageContentStream.endText();
                pageContentStream.moveTo(0, 0);
            }

            pageContentStream.close();

            return pdDocument;
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void createPrintReceiptJob(PDDocument receiptDocument) {

        PrinterJob job = PrinterJob.getPrinterJob();

        job.setPageable(new PDFPageable(receiptDocument));

        // Define custom paper
        Paper paper = new Paper();

        paper.setSize(728.50F, 290.50F);
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());

        // Custom page format
        PageFormat pageFormat = new PageFormat();

        pageFormat.setPaper(paper);

        // Override the page format
        Book book = new Book();

        // Append all pages
        book.append(new PDFPrintable(receiptDocument), pageFormat, receiptDocument.getNumberOfPages());
        job.setPageable(book);

        try {

            if (job.printDialog()) job.print();

            receiptDocument.close();
        }
        catch (PrinterException | IOException e) { throw new RuntimeException(e); }
    }
}