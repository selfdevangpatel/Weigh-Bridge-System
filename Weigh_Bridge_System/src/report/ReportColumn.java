package report;

public class ReportColumn {

    static ColumnInfoRecord getColumnInfoRecord(int reportFormatIndex, boolean showVehicleNumberColumn,
                                                boolean showPaymentModeColumn) {

        String[][] columnNames = new String[2][];
        int[] xOffset = new int[]{};
        int fontSize = 0;

        if (showVehicleNumberColumn && showPaymentModeColumn) {

            switch (reportFormatIndex) {

                case 0 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Gross", "Net"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Weight", "Weight"};
                    xOffset = new int[]{15, 85, 225, 315, 415, 515};
                    fontSize = 16;
                }
                case 1 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Gross", "Net", "Payment"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Weight", "Weight", "Mode"};
                    xOffset = new int[]{15, 80, 210, 280, 355, 430, 495};
                    fontSize = 15;
                }
                case 2 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Tare", "Gross", "Gross",
                                                  "Net"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Date", "Weight", "Date", "Weight"};
                    xOffset = new int[]{15, 75, 185, 245, 300, 385, 440, 530};
                    fontSize = 14;
                }
                case 3 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Tare", "Gross", "Gross",
                                                  "Net", "Payment"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Date", "Weight", "Date", "Weight",
                                                  "Mode"};
                    xOffset = new int[]{15, 67, 170, 220, 270, 345, 395, 475, 525};
                    fontSize = 13;
                }
                case 4 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Tare", "Tare", "Gross",
                                                  "Gross", "Gross", "Net"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Date", "Time", "Weight", "Date", "Time",
                                                  "Weight"};
                    xOffset = new int[]{15, 55, 140, 185, 228, 288, 362, 405, 465, 535};
                    fontSize = 11;
                }
                case 5 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Tare", "Tare", "Gross",
                                                  "Gross", "Gross", "Net", "Payment"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Date", "Time", "Weight", "Date", "Time",
                                                  "Weight", "Mode"};
                    xOffset = new int[]{15, 52, 130, 170, 210, 265, 335, 375, 435, 500, 540};
                    fontSize = 10;
                }
            }
        }
        else if (showVehicleNumberColumn) {

            switch (reportFormatIndex) {

                case 0, 1 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Gross", "Net"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Weight", "Weight"};
                    xOffset = new int[]{15, 85, 225, 315, 415, 515};
                    fontSize = 16;
                }
                case 2, 3 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Tare", "Gross", "Gross",
                                                  "Net"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Date", "Weight", "Date", "Weight"};
                    xOffset = new int[]{15, 75, 185, 245, 300, 385, 440, 530};
                    fontSize = 14;
                }
                case 4, 5 -> {

                    columnNames[0] = new String[]{"Serial", "Vehicle", "Charge", "Tare", "Tare", "Tare", "Gross",
                                                  "Gross", "Gross", "Net"};
                    columnNames[1] = new String[]{"No.", "No.", "", "Weight", "Date", "Time", "Weight", "Date", "Time",
                                                  "Weight"};
                    xOffset = new int[]{15, 55, 140, 185, 228, 288, 362, 405, 465, 535};
                    fontSize = 11;
                }
            }
        }
        else if (showPaymentModeColumn) {

            switch (reportFormatIndex) {

                case 0 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Gross", "Net"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Weight", "Weight"};
                    xOffset = new int[]{15, 85, 175, 275, 375};
                    fontSize = 16;
                }
                case 1 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Gross", "Net", "Payment"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Weight", "Weight", "Mode"};
                    xOffset = new int[]{15, 85, 175, 260, 345, 430};
                    fontSize = 16;
                }
                case 2 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Tare", "Gross", "Gross", "Net"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Date", "Weight", "Date", "Weight"};
                    xOffset = new int[]{15, 85, 150, 215, 315, 380, 480};
                    fontSize = 15;
                }
                case 3 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Tare", "Gross", "Gross", "Net",
                                                  "Payment"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Date", "Weight", "Date", "Weight", "Mode"};
                    xOffset = new int[]{15, 85, 150, 210, 300, 360, 455, 515};
                    fontSize = 15;
                }
                case 4 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Tare", "Tare", "Gross", "Gross", "Gross",
                                                  "Net"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Date", "Time", "Weight", "Date", "Time",
                                                  "Weight"};
                    xOffset = new int[]{15, 65, 118, 168, 238, 322, 372, 442, 530};
                    fontSize = 13;
                }
                case 5 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Tare", "Tare", "Gross", "Gross", "Gross",
                                                  "Net", "Payment"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Date", "Time", "Weight", "Date", "Time",
                                                  "Weight", "Mode"};
                    xOffset = new int[]{15, 60, 110, 150, 210, 285, 325, 385, 460, 520};
                    fontSize = 11;
                }
            }
        }
        else {

            switch (reportFormatIndex) {

                case 0, 1 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Gross", "Net"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Weight", "Weight"};
                    xOffset = new int[]{15, 85, 175, 275, 375};
                    fontSize = 16;
                }
                case 2, 3 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Tare", "Gross", "Gross", "Net"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Date", "Weight", "Date", "Weight"};
                    xOffset = new int[]{15, 85, 150, 215, 315, 380, 480};
                    fontSize = 15;
                }
                case 4, 5 -> {

                    columnNames[0] = new String[]{"Serial", "Charge", "Tare", "Tare", "Tare", "Gross", "Gross", "Gross",
                                                  "Net"};
                    columnNames[1] = new String[]{"No.", "", "Weight", "Date", "Time", "Weight", "Date", "Time",
                                                  "Weight"};
                    xOffset = new int[]{15, 65, 118, 168, 238, 322, 372, 442, 530};
                    fontSize = 13;
                }
            }
        }

        return new ColumnInfoRecord(columnNames, xOffset, fontSize);
    }
}