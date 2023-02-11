package database.query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

import database.Connection;
import datetime.DateTime;

import main.record.EntryRecord;

public class FindQuery {

    public static ArrayList<Integer> getSerialNumberList(String query) {

        try {

            ArrayList<Integer> serialNumberList = new ArrayList<>();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            statement.close();

            while(resultSet.next())
                serialNumberList.add(resultSet.getInt(1));

            return serialNumberList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static ObservableList<EntryRecord> getEntryRecordOList(ArrayList<Integer> serialNumberList) {

        try {

            ObservableList<EntryRecord> entryRecordOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet;

            for (Integer serialNumber : serialNumberList) {

                resultSet = statement.executeQuery("SELECT * FROM Entry WHERE Serial_Number = " + serialNumber);

                if (resultSet.next()) {

                    int tareWeight = resultSet.getInt(8);
                    Date tareDate = resultSet.getDate(9);
                    Time tareTime = resultSet.getTime(10);

                    int grossWeight = resultSet.getInt(12);
                    Date grossDate = resultSet.getDate(13);
                    Time grossTime = resultSet.getTime(14);

                    int netWeight = resultSet.getInt(16);

                    entryRecordOList.add(new EntryRecord(resultSet.getInt(1), resultSet.getString(2),
                                                         resultSet.getInt(3), resultSet.getString(4),
                                                         resultSet.getString(5), resultSet.getString(6),
                                                         resultSet.getString(7),
                                                         tareWeight == 0 ? "" : String.valueOf(tareWeight),
                                                         tareDate == null ? null : DateTime.dateToString(tareDate),
                                                         tareTime == null ? null : DateTime.timeToString(tareTime).
                                                                                            toUpperCase(),
                                                         resultSet.getString(11),
                                                         grossWeight == 0 ? "" : String.valueOf(grossWeight),
                                                         grossDate == null ? null : DateTime.dateToString(grossDate),
                                                         grossTime == null ? null : DateTime.timeToString(grossTime).
                                                                                             toUpperCase(),
                                                         resultSet.getString(15),
                                                         netWeight == 0 ? "" : String.valueOf(netWeight),
                                                         DateTime.dateToString(resultSet.getDate(17)),
                                                         DateTime.timeToString(resultSet.getTime(18)).toUpperCase(),
                                                         resultSet.getString(19), resultSet.getString(20)));
                }
            }

            statement.close();

            return entryRecordOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}