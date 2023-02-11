package database.query;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import static java.util.stream.Collectors.*;

import analytics.DateChargeRecord;
import analytics.MonthChargeRecord;

import database.Connection;

public class AnalyticsQuery {

    public static Map<Date, Integer> getTotalChargeByDate(Date dateFrom, Date dateTo) {

        try {

            List<DateChargeRecord> dateChargeRecordList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT Entry_Date, Charge FROM Entry WHERE (Entry_Date " +
                                                         "BETWEEN #" + dateFrom + "# AND #" + dateTo + "#)");

            statement.close();

            while (resultSet.next())
                dateChargeRecordList.add(new DateChargeRecord(resultSet.getDate(1), resultSet.getInt(2)));

            return dateChargeRecordList.stream().collect(groupingBy(DateChargeRecord::date,
                                                                    summingInt(DateChargeRecord::charge)));
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static Map<Date, Long> getEntryCountByDate(Date dateFrom, Date dateTo) {

        try {

            List<Date> dateList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT Entry_Date FROM Entry WHERE (Entry_Date BETWEEN #" +
                                                         dateFrom + "# AND #" + dateTo + "#)");

            statement.close();

            while (resultSet.next())
                dateList.add(resultSet.getDate(1));

            return dateList.stream().collect(groupingBy(date -> date, counting()));
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static Object[] getEntryCountBy24Hours(Date day) {

        try {

            ArrayList<Time> timeList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT Entry_Time FROM Entry WHERE Entry_Date = #" + day +
                                                         "#");

            statement.close();

            while (resultSet.next())
                timeList.add(resultSet.getTime(1));

            String[] hourNames = new String[]{"0 AM - 1 AM", "1 AM - 2 AM", "2 AM - 3 AM", "3 AM - 4 AM", "4 AM - 5 AM",
                                              "5 AM - 6 AM", "6 AM - 7 AM", "7 AM - 8 AM", "8 AM - 9 AM",
                                              "9 AM - 10 AM", "10 AM - 11 AM", "11 AM - 12 NOON", "12 NOON - 1 PM",
                                              "1 PM - 2 PM", "2 PM - 3 PM", "3 PM - 4 PM", "4 PM - 5 PM", "5 PM - 6 PM",
                                              "6 PM - 7 PM", "7 PM - 8 PM", "8 PM - 9 PM", "9 PM - 10 PM",
                                              "10 PM - 11 PM", "11 PM - 12 PM"
            };

            int[] timeCount = new int[24];

            for (Time time : timeList) {

                String timeString = time.toString().substring(0, 2);

                switch (timeString) {

                    case "00" -> timeCount[0]++;
                    case "01" -> timeCount[1]++;
                    case "02" -> timeCount[2]++;
                    case "03" -> timeCount[3]++;
                    case "04" -> timeCount[4]++;
                    case "05" -> timeCount[5]++;
                    case "06" -> timeCount[6]++;
                    case "07" -> timeCount[7]++;
                    case "08" -> timeCount[8]++;
                    case "09" -> timeCount[9]++;
                    case "10" -> timeCount[10]++;
                    case "11" -> timeCount[11]++;
                    case "12" -> timeCount[12]++;
                    case "13" -> timeCount[13]++;
                    case "14" -> timeCount[14]++;
                    case "15" -> timeCount[15]++;
                    case "16" -> timeCount[16]++;
                    case "17" -> timeCount[17]++;
                    case "18" -> timeCount[18]++;
                    case "19" -> timeCount[19]++;
                    case "20" -> timeCount[20]++;
                    case "21" -> timeCount[21]++;
                    case "22" -> timeCount[22]++;
                    default -> timeCount[23]++;
                }
            }

            return new Object[]{hourNames, timeCount};
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static Map<String, Long> getStateWiseEntryCount(Date dateFrom, Date dateTo) {

        try {

            ArrayList<String> states = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Vehicle_Number FROM Entry WHERE (Entry_Date " +
                                                         "BETWEEN #" + dateFrom + "# AND #" + dateTo + "#)");

            statement.close();

            while(resultSet.next())
                states.add(resultSet.getString(1).substring(0, 2));

            return states.stream().collect(groupingBy(str -> str, counting()));
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static Map<String, Long> getPartyEntryCount(Date dateFrom, Date dateTo) {

        try {

            ArrayList<String> partyList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Party FROM Entry WHERE (Entry_Date BETWEEN #" +
                                                         dateFrom + "# AND #" + dateTo + "#)");

            statement.close();

            String partyName;

            while(resultSet.next()) {

                partyName = resultSet.getString(1);

                if (partyName != null) partyList.add(resultSet.getString(1));
            }

            return partyList.stream().collect(groupingBy(str -> str, counting()));
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static Map<String, Long> getMaterialEntryCount(Date dateFrom, Date dateTo) {

        try {

            ArrayList<String> materialList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Material FROM Entry WHERE (Entry_Date BETWEEN #" +
                                                         dateFrom + "# AND #" + dateTo + "#)");

            statement.close();

            String material;

            while(resultSet.next()) {

                material = resultSet.getString(1);

                if (material != null) materialList.add(material);
            }

            return materialList.stream().collect(groupingBy(str -> str, counting()));
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static Map<String, Integer> getMonthWiseTotalCharge(int selectedYear) {

        try {

            ArrayList<MonthChargeRecord> monthChargeList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MONTH(Entry_Date), Charge From Entry WHERE " +
                                                         "YEAR(Entry_Date) = '" + selectedYear + "'");

            statement.close();

            while (resultSet.next()) {

                int monthIndex = resultSet.getInt(1);
                int charge = resultSet.getInt(2);
                String monthString = "";

                switch (monthIndex) {

                    case 1 -> monthString = "January";
                    case 2 -> monthString = "February";
                    case 3 -> monthString = "March";
                    case 4 -> monthString = "April";
                    case 5 -> monthString = "May";
                    case 6 -> monthString = "June";
                    case 7 -> monthString = "July";
                    case 8 -> monthString = "August";
                    case 9 -> monthString = "September";
                    case 10 -> monthString = "October";
                    case 11 -> monthString = "November";
                    case 12 -> monthString = "December";
                }

                monthChargeList.add(new MonthChargeRecord(monthString, charge)); }

            return monthChargeList.stream().collect(groupingBy(MonthChargeRecord::month,
                                                               summingInt(MonthChargeRecord::charge)));
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static Object[] getOperatorWiseEntryCount(Date day) {

        try {

            List<String> insertOperatorList = new ArrayList<>();
            List<String> updateOperatorList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();

            ResultSet insertOperatorRS = statement.executeQuery("SELECT Insert_Operator From Entry Where Entry_Date" +
                                                                " = #" + day+ "#");

            statement.close();

            while (insertOperatorRS.next())
                insertOperatorList.add(insertOperatorRS.getString(1));

            statement = Connection.connection.createStatement();

            ResultSet updateOperatorRS = statement.executeQuery("SELECT Update_Operator From Entry Where Entry_Date" +
                                                                " = #" + day + "#");

            statement.close();

            while (updateOperatorRS.next()) {

                String operatorName = updateOperatorRS.getString(1);

                if (operatorName != null) updateOperatorList.add(updateOperatorRS.getString(1));
            }

            Map<String, Long> insertOperatorMap = insertOperatorList.stream().collect(groupingBy(s -> s, counting()));
            Map<String, Long> updateOperatorMap = updateOperatorList.stream().collect(groupingBy(s -> s, counting()));

            return new Object[]{insertOperatorMap, updateOperatorMap};
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}