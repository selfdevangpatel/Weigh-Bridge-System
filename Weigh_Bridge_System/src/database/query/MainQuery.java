package database.query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import database.Connection;

import main.record.EntryFieldRecord;
import main.record.TareGrossWeightRecord;

import print.ReceiptRecord;

public class MainQuery {

    public static Integer getNextAutoIncrementID() {

        try {

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT NAI FROM Misc WHERE ID = 1");

            statement.close();

            if (resultSet.next())
                return resultSet.getInt(1);
        }
        catch (SQLException e) { throw new RuntimeException(e); }

        return null;
    }

    public static void setNextAutoIncrementID() {

        try {

            PreparedStatement preparedStatement = Connection.connection.prepareStatement("UPDATE Misc SET NAI = NAI " +
                                                                                         "+ 1 WHERE ID = 1");

            preparedStatement.execute();
            preparedStatement.close();
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static ObservableList<Integer> getTareEntrySerialNoOList() {

        try {

            ObservableList<Integer> tareEntrySerialNoOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Serial_Number FROM Entry WHERE Gross_Weight IS NULL");

            statement.close();

            while (resultSet.next())
                tareEntrySerialNoOList.add(resultSet.getInt(1));

            return tareEntrySerialNoOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static ObservableList<Integer> getGrossEntrySerialNoOList() {

        try {

            ObservableList<Integer> grossEntrySerialNoOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Serial_Number FROM Entry WHERE Tare_Weight IS NULL");

            statement.close();

            while (resultSet.next())
                grossEntrySerialNoOList.add(resultSet.getInt(1));

            return grossEntrySerialNoOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean insertEntry(String vehicleNumber, int charge, String party, String paymentMode,
                                      String containerNumber, String material,
                                      int tareWeight, Date tareDate, Time tareTime, String tareManual,
                                      int grossWeight, Date grossDate, Time grossTime, String grossManual,
                                      int netWeight, Date entryDate, Time entryTime, String operator) {

        try {

            String insertQuery = "INSERT INTO Entry(Vehicle_Number, Charge, Party, Payment_Mode, Container_Number, " +
                                 "Material, Tare_Weight, Tare_Date, Tare_Time, Tare_Manual, Gross_Weight, " +
                                 "Gross_Date, Gross_Time, Gross_Manual, Net_Weight, Entry_Date, Entry_Time, " +
                                 "Insert_Operator) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, vehicleNumber);
            preparedStatement.setInt(2, charge);
            preparedStatement.setString(3, party);
            preparedStatement.setString(4, paymentMode);
            preparedStatement.setString(5, containerNumber);
            preparedStatement.setString(6, material);

            if (tareWeight == 0) {

                preparedStatement.setNull(7, Types.INTEGER);
                preparedStatement.setNull(8, Types.DATE);
                preparedStatement.setNull(9, Types.TIME);
                preparedStatement.setNull(10, Types.VARCHAR);
            }
            else {

                preparedStatement.setInt(7, tareWeight);
                preparedStatement.setDate(8, tareDate);
                preparedStatement.setTime(9, tareTime);
                preparedStatement.setString(10, tareManual);
            }

            if (grossWeight == 0) {

                preparedStatement.setNull(11, Types.INTEGER);
                preparedStatement.setNull(12, Types.DATE);
                preparedStatement.setNull(13, Types.TIME);
                preparedStatement.setNull(14, Types.VARCHAR);
            }
            else {

                preparedStatement.setInt(11, grossWeight);
                preparedStatement.setDate(12, grossDate);
                preparedStatement.setTime(13, grossTime);
                preparedStatement.setString(14, grossManual);
            }

            if (netWeight == 0) preparedStatement.setNull(15, Types.INTEGER);
            else preparedStatement.setInt(15, netWeight);

            preparedStatement.setDate(16, entryDate);
            preparedStatement.setTime(17, entryTime);
            preparedStatement.setString(18, operator);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean serialNumberExists(int serialNumber) {

        try {

            Statement statement = Connection.connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT TOP 1 1 FROM Entry WHERE Serial_Number = " +
                                                         serialNumber);

            statement.close();

            return resultSet.next();
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static EntryFieldRecord getEntryFieldRecord(int serialNumber) {

        try {

            String selectQuery = "SELECT Vehicle_Number, Charge, Party, Container_Number, Payment_Mode, Material, " +
                                 "Tare_Weight, Tare_Date, Tare_Time, Tare_Manual, Gross_Weight, Gross_Date, " +
                                 "Gross_Time, Gross_Manual, Net_Weight FROM Entry WHERE Serial_Number = " +
                                 serialNumber;

            Statement statement = Connection.connection.createStatement();

            ResultSet resultSet = statement.executeQuery(selectQuery);

            statement.close();

            if (resultSet.next())
                return new EntryFieldRecord(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3),
                                            resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
                                            resultSet.getInt(7), resultSet.getDate(8), resultSet.getTime(9),
                                            resultSet.getString(10), resultSet.getInt(11), resultSet.getDate(12),
                                            resultSet.getTime(13), resultSet.getString(14), resultSet.getInt(15));
        }
        catch (SQLException e) { throw new RuntimeException(e); }

        return null;
    }

    public static TareGrossWeightRecord getTareAndGrossWeight(int serialNumber) {

        try {

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Tare_Weight, Gross_Weight FROM Entry WHERE " +
                                                         "Serial_Number = " + serialNumber);

            statement.close();

            if (resultSet.next())
                return new TareGrossWeightRecord(resultSet.getInt(1), resultSet.getInt(2));
        }
        catch (SQLException e) { throw new RuntimeException(e); }

        return null;
    }

    public static boolean updateTareWeight(int tareWeight, Date tareDate, Time tareTime, String tareManual,
                                           int netWeight, String updateOperator, int serialNumber) {

        try {

            String updateQuery = "UPDATE Entry SET Tare_Weight = ?, Tare_Date = ?, Tare_Time = ?, Tare_Manual = ?, " +
                                 "Net_Weight = ?, Update_Operator = ? WHERE Serial_Number = ?";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(updateQuery);

            preparedStatement.setInt(1, tareWeight);
            preparedStatement.setDate(2, tareDate);
            preparedStatement.setTime(3, tareTime);
            preparedStatement.setString(4, tareManual);

            if (netWeight == 0) preparedStatement.setNull(5, Types.INTEGER);
            else preparedStatement.setInt(5, netWeight);

            preparedStatement.setString(6, updateOperator);
            preparedStatement.setInt(7, serialNumber);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean updateGrossWeight(int grossWeight, Date grossDate, Time grossTime, String grossManual,
                                           int netWeight, String updateOperator, int serialNumber) {

        try {

            String updateQuery = "UPDATE Entry SET Gross_Weight = ?, Gross_Date = ?, Gross_Time = ?, " +
                                 "Gross_Manual = ?, Net_Weight = ?, Update_Operator = ? WHERE Serial_Number = ?";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(updateQuery);

            preparedStatement.setInt(1, grossWeight);
            preparedStatement.setDate(2, grossDate);
            preparedStatement.setTime(3, grossTime);
            preparedStatement.setString(4, grossManual);

            if (netWeight == 0) preparedStatement.setNull(5, Types.INTEGER);
            else preparedStatement.setInt(5, netWeight);

            preparedStatement.setString(6, updateOperator);
            preparedStatement.setInt(7, serialNumber);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean updateVehicleInfo(String vehicleNumber, Integer charge, String party, String containerNumber,
                                            String paymentMode, String material, String updateOperator,
                                            int serialNumber) {

        try {

            String updateQuery = "UPDATE Entry SET Vehicle_Number = ?, Charge = ?, Party = ?, Container_Number = ?, " +
                                 "Payment_Mode = ?, Material = ?, Update_Operator = ? WHERE Serial_Number = ?";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, vehicleNumber);
            preparedStatement.setInt(2, charge);
            preparedStatement.setString(3, party);
            preparedStatement.setString(4, containerNumber);
            preparedStatement.setString(5, paymentMode);
            preparedStatement.setString(6, material);
            preparedStatement.setString(7, updateOperator);
            preparedStatement.setInt(8, serialNumber);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static int getLastSerialNumber() {

        try {

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT LAST(Serial_Number) FROM Entry");

            statement.close();

            if (resultSet.next())
                return resultSet.getInt(1);
        }
        catch (SQLException e) { throw new RuntimeException(e); }

        return 0;
    }

    public static ReceiptRecord getReceiptRecord(int serialNumber) {

        try {

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Vehicle_Number, Charge, Party, Container_Number, " +
                                                         "Tare_Weight, Tare_Date, Tare_Time, Gross_Weight, " +
                                                         "Gross_Date, Gross_Time, Net_Weight FROM Entry WHERE " +
                                                         "Serial_Number = " + serialNumber);

            statement.close();

            if (resultSet.next())
                return new ReceiptRecord(serialNumber, resultSet.getString(1), resultSet.getInt(2),
                                         resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5),
                                         resultSet.getDate(6), resultSet.getTime(7), resultSet.getInt(8),
                                         resultSet.getDate(9), resultSet.getTime(10), resultSet.getInt(11));
        }
        catch (SQLException e) { throw new RuntimeException(e); }

        return null;
    }

    public static boolean deleteEntry(int serialNumber) {

        try {

            String deleteQuery = "DELETE FROM Entry WHERE Serial_Number = " + serialNumber;
            PreparedStatement preparedStatement = Connection.connection.prepareStatement(deleteQuery);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}