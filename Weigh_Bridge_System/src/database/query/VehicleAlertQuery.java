package database.query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import database.Connection;

import main.record.VehicleAlertRecord;

public class VehicleAlertQuery {

    public static ObservableList<VehicleAlertRecord> getVehicleAlertOList() {

        try {

            ObservableList<VehicleAlertRecord> vehicleAlertRecordOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Vehicle_Number, Note FROM VehicleAlert");

            statement.close();

            while (resultSet.next())
                vehicleAlertRecordOList.add(new VehicleAlertRecord(resultSet.getString(1), resultSet.getString(2)));

            return vehicleAlertRecordOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean addVehicle(String vehicleNumber, String note) {

        try {

            String insertQuery = "INSERT INTO VehicleAlert(Vehicle_Number, Note) VALUES(?, ?)";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, vehicleNumber);
            preparedStatement.setString(2, note);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean removeVehicle(String vehicleNumber) {

        try {

            String deleteQuery = "DELETE FROM VehicleAlert WHERE Vehicle_Number = ?";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(deleteQuery);

            preparedStatement.setString(1, vehicleNumber);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}