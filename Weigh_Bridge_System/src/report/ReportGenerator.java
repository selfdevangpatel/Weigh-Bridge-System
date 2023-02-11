package report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import database.query.ReportQuery;

import datetime.DateTime;

public class ReportGenerator {

    private static int pageCount;
    private static int startNewLineFrom;
    private static PDDocument pdDocument;
    private static final PDRectangle PD_RECTANGLE = new PDRectangle(728.50F, 864.50F);
    private static PDPageContentStream pdPageContentStream;

    static PDDocument createReport(ReportInfoRecord reportInfoRecord, ArrayList<Integer> serialNumberList,
                                   int reportFormatIndex, boolean selectedPartyReport) {

        try {

            pdDocument = new PDDocument();
            pageCount = 0;

            pdDocument.addPage(new PDPage(PD_RECTANGLE));

            pdPageContentStream = new PDPageContentStream(pdDocument, pdDocument.getPage(pageCount));

            setHeader();
            setReportInformation(reportInfoRecord);

            boolean showVehicleNumberColumn = reportInfoRecord.vehicleNumber().equals("Any");
            boolean showPaymentModeColumn = reportInfoRecord.paymentMode().equals("Any");
            ColumnInfoRecord columnInfoRecord = ReportColumn.getColumnInfoRecord(reportFormatIndex,
                                                                                 showVehicleNumberColumn,
                                                                                 showPaymentModeColumn);

            setColumnNames(columnInfoRecord.columnNames(), columnInfoRecord.xOffset(), columnInfoRecord.fontSize());

            Map<String, List<String[]>> partyNamePartyEntryMap = ReportQuery.
                                                                 getPartyNamePartyEntryMap(serialNumberList,
                                                                                           reportFormatIndex,
                                                                                           showVehicleNumberColumn,
                                                                                           showPaymentModeColumn);
            int chargeIndex = showVehicleNumberColumn ? 2 : 1;

            setEntryContent(partyNamePartyEntryMap, chargeIndex, columnInfoRecord.xOffset(),
                            columnInfoRecord.fontSize(), selectedPartyReport);
            setPageNumber();

            // Set value for Excel report
            ExcelReport.setReportFormatIndex(reportFormatIndex);
            ExcelReport.setReportInfoRecord(reportInfoRecord);
            ExcelReport.setColumnNames(columnInfoRecord.columnNames());
            ExcelReport.setPartyNamePartyEntryMap(partyNamePartyEntryMap);
            ExcelReport.setSelectedPartyReport(selectedPartyReport);
            ExcelReport.setChargeIndex(chargeIndex);

            // Return document
            return pdDocument;
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    private static void setHeader() {

        try {

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_BOLD, 20);
            pdPageContentStream.newLineAtOffset(188.11F, 819.34F);
            pdPageContentStream.showText("WEIGH-BRIDGE SYSTEM");
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
            pdPageContentStream.newLineAtOffset(200.11F, 799.31F);
            pdPageContentStream.showText("Address: StreetABC, CityXYZ");
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
            pdPageContentStream.newLineAtOffset(238.60F, 779.31F);
            pdPageContentStream.showText("Report Document");
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    private static void setReportInformation(ReportInfoRecord reportInfoRecord) {

        try {

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_BOLD, 16);
            pdPageContentStream.newLineAtOffset(15.0F, 737.4F);
            pdPageContentStream.showText("Report information:");
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
            pdPageContentStream.newLineAtOffset(15.0F, 717.4F);
            pdPageContentStream.showText("Serial No.: " + reportInfoRecord.fromSerialNumber() + "  to  " +
                                         reportInfoRecord.toSerialNumber());
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
            pdPageContentStream.newLineAtOffset(305.0F, 717.4F);
            pdPageContentStream.showText("Date: " + reportInfoRecord.fromDate() + "  to  " + reportInfoRecord.toDate());
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
            pdPageContentStream.newLineAtOffset(15.0F, 697.4F);
            pdPageContentStream.showText("Vehicle No.: " + reportInfoRecord.vehicleNumber());
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
            pdPageContentStream.newLineAtOffset(305.0F, 697.4F);
            pdPageContentStream.showText("Payment Mode: " + reportInfoRecord.paymentMode());
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
            pdPageContentStream.newLineAtOffset(15.0F, 677.4F);
            pdPageContentStream.showText("Entry Status: " + reportInfoRecord.entryStatus());
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
            pdPageContentStream.newLineAtOffset(15.0F, 657.4F);
            pdPageContentStream.showText("Party: " + reportInfoRecord.party());
            pdPageContentStream.endText();
            pdPageContentStream.moveTo(0, 0);
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    private static void setColumnNames(String[][] columnNames, int[] xOffset, int fontSize) {

        try {

            for (int i = 0; i < columnNames[0].length; i++) {

                pdPageContentStream.beginText();
                pdPageContentStream.setFont(PDType1Font.TIMES_BOLD, fontSize);
                pdPageContentStream.newLineAtOffset(xOffset[i], 620);
                pdPageContentStream.showText(columnNames[0][i]);
                pdPageContentStream.endText();
            }

            for (int i = 0; i < columnNames[1].length; i++) {

                pdPageContentStream.beginText();
                pdPageContentStream.setFont(PDType1Font.TIMES_BOLD, fontSize);
                pdPageContentStream.newLineAtOffset(xOffset[i], 600);
                pdPageContentStream.showText(columnNames[1][i]);
                pdPageContentStream.endText();
            }
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    private static void setEntryContent(Map<String, List<String[]>> partyNamePartyEntryMap, int chargeIndex,
                                        int[] xOffset, int fontSize, boolean selectedPartyReport) {

        try {

            startNewLineFrom = 570;

            if (partyNamePartyEntryMap == null) return ;

            // Entry without party
            if (partyNamePartyEntryMap.containsKey("")) {

                List<String[]> emptyPartyEntryList = partyNamePartyEntryMap.get("");
                int totalCharge = 0;

                for (String[] entry : emptyPartyEntryList) {

                    totalCharge += Integer.parseInt(entry[chargeIndex]);

                    if (startNewLineFrom < 32) addNewPage();

                    for (int i = 0; i < entry.length - 1; i++) {

                        pdPageContentStream.beginText();
                        pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, fontSize);
                        pdPageContentStream.newLineAtOffset(xOffset[i], startNewLineFrom);
                        pdPageContentStream.showText(entry[i]);
                        pdPageContentStream.endText();
                    }

                    startNewLineFrom -= 20;
                }

                startNewLineFrom -= 5;

                if (startNewLineFrom < 32) addNewPage();

                pdPageContentStream.beginText();
                pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, fontSize);
                pdPageContentStream.newLineAtOffset(15, startNewLineFrom);
                pdPageContentStream.showText("Total charge : " + totalCharge + " (" + emptyPartyEntryList.size() + ")");
                pdPageContentStream.endText();

                startNewLineFrom -= 40;
            }

            // Entry with party
            for(Map.Entry<String, List<String[]>> map : partyNamePartyEntryMap.entrySet()) {

                String partyName = map.getKey();

                if (partyName.equals("")) continue;

                List<String[]> partyEntryList = map.getValue();
                int totalCharge = 0;

                if (!selectedPartyReport) {

                    if (startNewLineFrom < 78) addNewPage();

                    pdPageContentStream.beginText();
                    pdPageContentStream.setFont(PDType1Font.TIMES_BOLD, fontSize);
                    pdPageContentStream.newLineAtOffset(15, startNewLineFrom);
                    pdPageContentStream.showText("Party : " + partyName);
                    pdPageContentStream.endText();

                    startNewLineFrom -= 20;
                }

                for (String[] columnFields : partyEntryList) {

                    totalCharge += Integer.parseInt(columnFields[chargeIndex]);

                    if (startNewLineFrom < 52) addNewPage();

                    for (int i = 0; i < columnFields.length - 1; i++) {

                        pdPageContentStream.beginText();
                        pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, fontSize);
                        pdPageContentStream.newLineAtOffset(xOffset[i], startNewLineFrom);
                        pdPageContentStream.showText(columnFields[i]);
                        pdPageContentStream.endText();
                    }

                    startNewLineFrom -= 20;
                }

                startNewLineFrom -= 5;

                if (startNewLineFrom < 32) addNewPage();

                pdPageContentStream.beginText();
                pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, fontSize);
                pdPageContentStream.newLineAtOffset(15, startNewLineFrom);
                pdPageContentStream.showText("Total charge : " + totalCharge + " (" + partyEntryList.size() + ")");
                pdPageContentStream.endText();

                startNewLineFrom -= 40;
            }

            if (startNewLineFrom < 32) addNewPage();

            pdPageContentStream.beginText();
            pdPageContentStream.setFont(PDType1Font.TIMES_BOLD, 13);
            pdPageContentStream.newLineAtOffset(15, startNewLineFrom);
            pdPageContentStream.showText("Report created on: " + DateTime.getCurrentDate() + "   " +
                                         DateTime.getCurrentTime().toUpperCase());
            pdPageContentStream.endText();

            pdPageContentStream.close();
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    private static void setPageNumber() {

        try {

            for (int i = 1; i < pageCount + 2; i++) {

                pdPageContentStream = new PDPageContentStream(pdDocument, pdDocument.getPage(i - 1),
                                                              PDPageContentStream.AppendMode.APPEND, false);

                pdPageContentStream.beginText();
                pdPageContentStream.setFont(PDType1Font.TIMES_BOLD, 13);
                pdPageContentStream.newLineAtOffset(510, 840);
                pdPageContentStream.showText("Page : " + i + "/" + (pageCount + 1));
                pdPageContentStream.endText();

                pdPageContentStream.close();
            }
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    private static void addNewPage() {

        try {

            pdPageContentStream.close();
            pdDocument.addPage(new PDPage(PD_RECTANGLE));
            pageCount++;

            pdPageContentStream = new PDPageContentStream(pdDocument, pdDocument.getPage(pageCount));
            startNewLineFrom = 800;
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }
}