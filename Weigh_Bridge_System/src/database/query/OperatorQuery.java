package database.query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import database.Connection;

import main.record.OperatorRecord;

public class OperatorQuery {

    public static ObservableList<OperatorRecord> getOperatorRecordOList() {

        try {

            ObservableList<OperatorRecord> operatorRecordOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Operator_Name, Contact_Number, Email_Address, " +
                                                         "Address FROM Operator");

            statement.close();

            while (resultSet.next())
                operatorRecordOList.add(new OperatorRecord(resultSet.getString(1), resultSet.getString(2),
                                                           resultSet.getString(3), resultSet.getString(4)));

            return operatorRecordOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean addOperator(String operatorName, String contactNumber, String emailAddress, String address) {

        try {

            String insertQuery = "INSERT INTO Operator(Operator_Name, Contact_Number, Email_Address, Address) VALUES" +
                                 "(?, ?, ?, ?)";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, operatorName);
            preparedStatement.setString(2, contactNumber);
            preparedStatement.setString(3, emailAddress);
            preparedStatement.setString(4, address);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean removeOperator(String operatorName) {

        try {

            String deleteQuery = "DELETE FROM Operator WHERE Operator_Name = ?";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(deleteQuery);

            preparedStatement.setString(1, operatorName);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static ObservableList<String> getOperatorOList() {

        try {

            ObservableList<String> operatorOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Operator_Name From Operator");

            statement.close();

            while (resultSet.next())
                operatorOList.add(resultSet.getString(1));

            return operatorOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}