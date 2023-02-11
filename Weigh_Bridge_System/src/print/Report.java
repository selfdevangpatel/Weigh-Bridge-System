package print;

import java.awt.print.*;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;

public class Report {

    public static void createPrintReportJob(PDDocument pdDocument) {

        PrinterJob job = PrinterJob.getPrinterJob();

        job.setPageable(new PDFPageable(pdDocument));

        // Define custom paper
        Paper paper = new Paper();

        paper.setSize(728.50F, 864.50F);
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());

        // Custom page format
        PageFormat pageFormat = new PageFormat();

        pageFormat.setPaper(paper);

        // Override the page format
        Book book = new Book();

        // Append all pages
        book.append(new PDFPrintable(pdDocument), pageFormat, pdDocument.getNumberOfPages());
        job.setPageable(book);

        try {

            if (job.printDialog()) job.print();

            pdDocument.close();
        }
        catch (PrinterException | IOException e) { throw new RuntimeException(e); }
    }
}