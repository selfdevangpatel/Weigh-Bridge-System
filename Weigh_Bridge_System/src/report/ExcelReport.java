package report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import datetime.DateTime;

public class ExcelReport {

    private static int reportFormatIndex;
    private static ReportInfoRecord reportInfoRecord;
    private static String[][] columnNames;
    private static Map<String, List<String[]>> partyNamePartyEntryMap;
    private static boolean selectedPartyReport;
    private static int chargeIndex;

    static boolean createExcelReport(String exportLocation) {

        if (partyNamePartyEntryMap == null) return false;

        int serialNumberFrom = reportInfoRecord.fromSerialNumber();
        int serialNumberTo = reportInfoRecord.toSerialNumber();
        String dateFrom = reportInfoRecord.fromDate();
        String dateTo = reportInfoRecord.toDate();
        String vehicleNumber = reportInfoRecord.vehicleNumber();
        String paymentMode = reportInfoRecord.paymentMode();
        String entryStatus = reportInfoRecord.entryStatus();
        String party = reportInfoRecord.party();

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("Weigh-Bridge System");

        XSSFRow row; XSSFCell cell;
        int mergeCellCount = reportFormatIndex + 5;

        // Create a new font and alter it.
        Font font = xssfWorkbook.createFont();
        font.setFontName("Calibri");
        font.setBold(true);

        CellStyle cellStyle = xssfWorkbook.createCellStyle();
        cellStyle.setFont(font);

        // Header
        row = xssfSheet.createRow(1); cell = row.createCell(0);

        cell.setCellValue("WEIGH-BRIDGE SYSTEM");
        cell.setCellStyle(cellStyle);
        xssfSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, mergeCellCount));
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

        row = xssfSheet.createRow(2); cell = row.createCell(0);

        cell.setCellValue("Address: StreetABC, CityXYZ");
        cell.setCellStyle(cellStyle);
        xssfSheet.addMergedRegion(new CellRangeAddress(2, 2, 0, mergeCellCount));
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

        row = xssfSheet.createRow(3); cell = row.createCell(0);

        cell.setCellValue("Report Document");
        cell.setCellStyle(cellStyle);
        xssfSheet.addMergedRegion(new CellRangeAddress(3, 3, 0, mergeCellCount));
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

        // Report information
        row = xssfSheet.createRow(5); cell = row.createCell(0);

        cell.setCellValue("Report Information :-");
        cell.setCellStyle(cellStyle);
        xssfSheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 1));

        row = xssfSheet.createRow(6); cell = row.createCell(0);

        cell.setCellValue("Serial No.: ");

        cell = row.createCell(1);

        cell.setCellValue(serialNumberFrom + " to " + serialNumberTo);
        xssfSheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 2));

        cell = row.createCell(4);

        cell.setCellValue("Date: ");

        cell = row.createCell(5);

        cell.setCellValue(dateFrom + " to " + dateTo);

        row = xssfSheet.createRow(7); cell = row.createCell(0);

        cell.setCellValue("Vehicle No.: ");

        cell = row.createCell(1);

        cell.setCellValue(vehicleNumber);
        xssfSheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 2));

        cell = row.createCell(4);

        cell.setCellValue("Payment Mode: ");

        cell = row.createCell(5);

        cell.setCellValue(paymentMode);

        row = xssfSheet.createRow(8); cell = row.createCell(0);

        cell.setCellValue("Entry Status: ");

        cell = row.createCell(1);

        cell.setCellValue(entryStatus);
        xssfSheet.addMergedRegion(new CellRangeAddress(8, 8, 1, mergeCellCount));

        row = xssfSheet.createRow(9); cell = row.createCell(0);

        cell.setCellValue("Party: ");

        cell = row.createCell(1);

        cell.setCellValue(party);
        xssfSheet.addMergedRegion(new CellRangeAddress(9, 9, 1, mergeCellCount));

        // Column names
        row = xssfSheet.createRow(11);

        for (int i = 0; i < columnNames[0].length; i++) {

            cell = row.createCell(i);

            cell.setCellValue(columnNames[0][i] + " " + columnNames[1][i]);
            cell.setCellStyle(cellStyle);
        }

        // Entry content
        row = xssfSheet.createRow(12);

        // Entry without party
        if (partyNamePartyEntryMap.containsKey("")) {

            List<String[]> emptyPartyEntryList = partyNamePartyEntryMap.get("");
            int totalCharge = 0;

            for (String[] entry : emptyPartyEntryList) {

                totalCharge += Integer.parseInt(entry[chargeIndex]);

                for (int i = 0; i < entry.length - 1; i++) {

                    cell = row.createCell(i);

                    cell.setCellValue(entry[i]);
                }

                row = xssfSheet.createRow(cell.getRowIndex() + 1);
            }

            cell = row.createCell(0);

            cell.setCellValue("Total charge: " + totalCharge + " (" + emptyPartyEntryList.size() + ")");
            cell.setCellStyle(cellStyle);

            int rowIndex = cell.getRowIndex();
            xssfSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, mergeCellCount));
        }

        // Entry with party
        row = xssfSheet.createRow(cell.getRowIndex() + 2);

        for (Map.Entry<String, List<String[]>> map : partyNamePartyEntryMap.entrySet()) {

            String partyName = map.getKey();

            if (partyName.equals("")) continue;

            List<String[]> partyEntry = map.getValue();
            int totalCharge = 0;

            if (!selectedPartyReport) {

                cell = row.createCell(0);

                cell.setCellValue("Party: " + partyName);
                cell.setCellStyle(cellStyle);

                int rowIndex = cell.getRowIndex();
                xssfSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, mergeCellCount));
            }

            for (String[] entry : partyEntry) {

                totalCharge += Integer.parseInt(entry[chargeIndex]);

                row = xssfSheet.createRow(cell.getRowIndex() + 1);

                for (int i = 0; i < entry.length - 1; i++) {

                    cell = row.createCell(i);

                    cell.setCellValue(entry[i]);
                }
            }

            int rowIndex = cell.getRowIndex();
            row = xssfSheet.createRow(rowIndex + 1);

            cell = row.createCell(0);

            cell.setCellValue("Total charge: " + totalCharge + " (" + partyEntry.size() + ")");
            cell.setCellStyle(cellStyle);
            xssfSheet.addMergedRegion(new CellRangeAddress(rowIndex + 1, rowIndex + 1, 0, mergeCellCount));

            row = xssfSheet.createRow(rowIndex + 3);
        }

        // For report date/time
        cell = row.createCell(0);

        cell.setCellValue("Report created on: " + DateTime.getCurrentDate() + "   " +
                          DateTime.getCurrentTime().toUpperCase());
        cell.setCellStyle(cellStyle);

        int rowIndex = cell.getRowIndex();
        xssfSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, mergeCellCount));

        // Export file
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(exportLocation);

            xssfWorkbook.write(fileOutputStream);

            fileOutputStream.close();

            return true;
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    static void setReportFormatIndex(int reportFormatIndex) {

        ExcelReport.reportFormatIndex = reportFormatIndex;
    }

    static void setReportInfoRecord(ReportInfoRecord reportInfoRecord) {

        ExcelReport.reportInfoRecord = reportInfoRecord;
    }

    static void setColumnNames(String[][] columnNames) {

        ExcelReport.columnNames = columnNames;
    }

    static void setPartyNamePartyEntryMap(Map<String, List<String[]>> partyNamePartyEntryMap) {

        ExcelReport.partyNamePartyEntryMap = partyNamePartyEntryMap;
    }

    static void setSelectedPartyReport(boolean selectedPartyReport) {

        ExcelReport.selectedPartyReport = selectedPartyReport;
    }

    static void setChargeIndex(int chargeIndex) {

        ExcelReport.chargeIndex = chargeIndex;
    }
}