package database.query;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import database.Connection;

import datetime.DateTime;

public class ReportQuery {

    public static ArrayList<Integer> getSerialNumberList(String query) {

        try {

            ArrayList<Integer> serialNumberList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            statement.close();

            while (resultSet.next())
                serialNumberList.add(resultSet.getInt(1));

            return serialNumberList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static Map<String, List<String[]>> getPartyNamePartyEntryMap(ArrayList<Integer> serialNumberList,
                                                                        int reportFormatIndex,
                                                                        boolean showVehicleNumberColumn,
                                                                        boolean showPaymentModeColumn) {

        List<String[]> partyEntryList = new ArrayList<>();

        try {

            Statement statement = Connection.connection.createStatement();

            if (showVehicleNumberColumn && showPaymentModeColumn) {

                switch (reportFormatIndex) {

                    case 0 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Gross_Weight, " +
                                                                         "Net_Weight, Party FROM Entry WHERE " +
                                                                         "Serial_Number = " + i);

                            String partyName;

                            while (resultSet.next()) {

                                partyName = resultSet.getString(7);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[6]));
                    }

                    case 1 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Gross_Weight, " +
                                                                         "Net_Weight, Payment_Mode, Party FROM Entry" +
                                                                         " WHERE Serial_Number = " + i);

                            String partyName;

                            while (resultSet.next()) {

                                partyName = resultSet.getString(8);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                resultSet.getString(7),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[7]));
                    }
                    case 2 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Tare_Date, " +
                                                                         "Gross_Weight, Gross_Date, Net_Weight, Party" +
                                                                         " FROM Entry WHERE Serial_Number = " + i);

                            Date tareDate, grossDate;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(5);
                                grossDate = resultSet.getDate(7);
                                partyName = resultSet.getString(9);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                String.valueOf(resultSet.getInt(8)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[8]));
                    }
                    case 3 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Tare_Date, " +
                                                                         "Gross_Weight, Gross_Date, Net_Weight, " +
                                                                         "Payment_Mode, Party FROM Entry WHERE " +
                                                                         "Serial_Number = " + i);

                            Date tareDate, grossDate;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(5);
                                grossDate = resultSet.getDate(7);
                                partyName = resultSet.getString(9);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                String.valueOf(resultSet.getInt(8)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[8]));
                    }
                    case 4 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Tare_Date, " +
                                                                         "Tare_Time, Gross_Weight, Gross_Date, " +
                                                                         "Gross_Time, Net_Weight, Party FROM Entry " +
                                                                         "WHERE Serial_Number = " + i);

                            Date tareDate, grossDate;
                            Time tareTime, grossTime;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(5);
                                tareTime = resultSet.getTime(6);
                                grossDate = resultSet.getDate(8);
                                grossTime = resultSet.getTime(9);
                                partyName = resultSet.getString(11);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                tareTime == null ? "" : DateTime.
                                                                                        timeToString(tareTime).
                                                                                        toUpperCase(),
                                                                String.valueOf(resultSet.getInt(7)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                grossTime == null ? "" : DateTime.
                                                                                         timeToString(grossTime).
                                                                                         toUpperCase(),
                                                                String.valueOf(resultSet.getInt(10)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[10]));
                    }
                    case 5 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Tare_Date, " +
                                                                         "Tare_Time, Gross_Weight, Gross_Date, " +
                                                                         "Gross_Time, Net_Weight, Payment_Mode, Party" +
                                                                         " FROM Entry WHERE Serial_Number = " + i);

                            Date tareDate, grossDate;
                            Time tareTime, grossTime;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(5);
                                tareTime = resultSet.getTime(6);
                                grossDate = resultSet.getDate(8);
                                grossTime = resultSet.getTime(9);
                                partyName = resultSet.getString(12);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                tareTime == null ? "" : DateTime.
                                                                                        timeToString(tareTime).
                                                                                        toUpperCase(),
                                                                String.valueOf(resultSet.getInt(7)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                grossTime == null ? "" : DateTime.
                                                                                         timeToString(grossTime).
                                                                                         toUpperCase(),
                                                                String.valueOf(resultSet.getInt(10)),
                                                                resultSet.getString(11),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[11]));
                    }
                }
            }
            else if (showVehicleNumberColumn) {

                switch (reportFormatIndex) {

                    case 0, 1 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Gross_Weight, " +
                                                                         "Net_Weight, Party FROM Entry WHERE " +
                                                                         "Serial_Number = " + i);

                            String partyName;

                            while (resultSet.next()) {

                                partyName = resultSet.getString(7);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[6]));
                    }
                    case 2, 3 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Tare_Date, " +
                                                                         "Gross_Weight, Gross_Date, Net_Weight, Party" +
                                                                         " FROM Entry WHERE Serial_Number = " + i);

                            Date tareDate, grossDate;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(5);
                                grossDate = resultSet.getDate(7);
                                partyName = resultSet.getString(9);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                String.valueOf(resultSet.getInt(8)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[8]));
                    }
                    case 4, 5 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Vehicle_Number, " +
                                                                         "Charge, Tare_Weight, Tare_Date, " +
                                                                         "Tare_Time, Gross_Weight, Gross_Date, " +
                                                                         "Gross_Time, Net_Weight, Party FROM Entry " +
                                                                         "WHERE Serial_Number = " + i);

                            Date tareDate, grossDate;
                            Time tareTime, grossTime;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(5);
                                tareTime = resultSet.getTime(6);
                                grossDate = resultSet.getDate(8);
                                grossTime = resultSet.getTime(9);
                                partyName = resultSet.getString(11);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                resultSet.getString(2),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                tareTime == null ? "" : DateTime.
                                                                                        timeToString(tareTime).
                                                                                        toUpperCase(),
                                                                String.valueOf(resultSet.getInt(7)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                grossTime == null ? "" : DateTime.
                                                                                         timeToString(grossTime).
                                                                                         toUpperCase(),
                                                                String.valueOf(resultSet.getInt(10)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[10]));
                    }
                }
            }
            else if (showPaymentModeColumn) {

                switch (reportFormatIndex) {

                    case 0 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Gross_Weight, Net_Weight, Party FROM" +
                                                                         " Entry WHERE Serial_Number = " + i);

                            String partyName;

                            while (resultSet.next()) {

                                partyName = resultSet.getString(6);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[5]));
                    }

                    case 1 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Gross_Weight, Net_Weight, Payment_Mode, " +
                                                                         "Party FROM Entry WHERE Serial_Number = " + i);

                            String partyName;

                            while (resultSet.next()) {

                                partyName = resultSet.getString(7);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                resultSet.getString(6),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[6]));
                    }
                    case 2 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Tare_Date, Gross_Weight, Gross_Date, " +
                                                                         "Net_Weight, Party FROM Entry WHERE " +
                                                                         "Serial_Number = " + i);

                            Date tareDate, grossDate;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(4);
                                grossDate = resultSet.getDate(6);
                                partyName = resultSet.getString(8);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                String.valueOf(resultSet.getInt(7)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[7]));
                    }
                    case 3 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Tare_Date, Gross_Weight, Gross_Date, " +
                                                                         "Net_Weight, Payment_Mode, Party FROM" +
                                                                         " Entry WHERE Serial_Number = " + i);

                            Date tareDate, grossDate;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(4);
                                grossDate = resultSet.getDate(6);
                                partyName = resultSet.getString(8);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                String.valueOf(resultSet.getInt(7)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[7]));
                    }
                    case 4 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Tare_Date, Tare_Time, Gross_Weight, " +
                                                                         "Gross_Date, Gross_Time, Net_Weight, Party " +
                                                                         "FROM Entry WHERE Serial_Number = " + i);

                            Date tareDate, grossDate;
                            Time tareTime, grossTime;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(4);
                                tareTime = resultSet.getTime(5);
                                grossDate = resultSet.getDate(7);
                                grossTime = resultSet.getTime(8);
                                partyName = resultSet.getString(10);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                tareTime == null ? "" : DateTime.
                                                                                        timeToString(tareTime).
                                                                                        toUpperCase(),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                grossTime == null ? "" : DateTime.
                                                                                         timeToString(grossTime).
                                                                                         toUpperCase(),
                                                                String.valueOf(resultSet.getInt(9)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[9]));
                    }
                    case 5 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Tare_Date, Tare_Time, Gross_Weight, " +
                                                                         "Gross_Date, Gross_Time, Net_Weight, " +
                                                                         "Payment_Mode, Party FROM Entry WHERE" +
                                                                         " Serial_Number = " + i);

                            Date tareDate, grossDate;
                            Time tareTime, grossTime;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(4);
                                tareTime = resultSet.getTime(5);
                                grossDate = resultSet.getDate(7);
                                grossTime = resultSet.getTime(8);
                                partyName = resultSet.getString(11);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                tareTime == null ? "" : DateTime.
                                                                                        timeToString(tareTime).
                                                                                        toUpperCase(),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                grossTime == null ? "" : DateTime.
                                                                                         timeToString(grossTime).
                                                                                         toUpperCase(),
                                                                String.valueOf(resultSet.getInt(9)),
                                                                resultSet.getString(10),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[10]));
                    }
                }
            }
            else {

                switch (reportFormatIndex) {

                    case 0, 1 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Gross_Weight, Net_Weight, Party FROM Entry" +
                                                                         " WHERE Serial_Number = " + i);

                            String partyName;

                            while (resultSet.next()) {

                                partyName = resultSet.getString(6);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                String.valueOf(resultSet.getInt(4)),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[5]));
                    }

                    case 2, 3 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Tare_Date, Gross_Weight, Gross_Date, " +
                                                                         "Net_Weight, Party FROM Entry WHERE " +
                                                                         "Serial_Number = " + i);

                            Date tareDate, grossDate;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(4);
                                grossDate = resultSet.getDate(6);
                                partyName = resultSet.getString(8);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                String.valueOf(resultSet.getInt(5)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                String.valueOf(resultSet.getInt(7)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[7]));
                    }
                    case 4, 5 -> {

                        for (Integer i : serialNumberList) {

                            ResultSet resultSet = statement.executeQuery("Select Serial_Number, Charge, Tare_Weight, " +
                                                                         "Tare_Date, Tare_Time, Gross_Weight, " +
                                                                         "Gross_Date, Gross_Time, Net_Weight, Party " +
                                                                         "FROM Entry WHERE Serial_Number = " + i);

                            Date tareDate, grossDate;
                            Time tareTime, grossTime;
                            String partyName;

                            while (resultSet.next()) {

                                tareDate = resultSet.getDate(4);
                                tareTime = resultSet.getTime(5);
                                grossDate = resultSet.getDate(7);
                                grossTime = resultSet.getTime(8);
                                partyName = resultSet.getString(10);

                                partyEntryList.add(new String[]{String.valueOf(resultSet.getInt(1)),
                                                                String.valueOf(resultSet.getInt(2)),
                                                                String.valueOf(resultSet.getInt(3)),
                                                                tareDate == null ? "" : DateTime.
                                                                                        dateToString(tareDate),
                                                                tareTime == null ? "" : DateTime.
                                                                                        timeToString(tareTime).
                                                                                        toUpperCase(),
                                                                String.valueOf(resultSet.getInt(6)),
                                                                grossDate == null ? "" : DateTime.
                                                                                         dateToString(grossDate),
                                                                grossTime == null ? "" : DateTime.
                                                                                         timeToString(grossTime).
                                                                                         toUpperCase(),
                                                                String.valueOf(resultSet.getInt(9)),
                                                                partyName == null ? "" : partyName});
                            }
                        }

                        statement.close();

                        return partyEntryList.stream().collect(Collectors.groupingBy(strings -> strings[9]));
                    }
                }
            }
        }
        catch (SQLException e) { throw new RuntimeException(e); }

        return null;
    }
}