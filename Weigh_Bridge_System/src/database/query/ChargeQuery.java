package database.query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.Connection;

public class ChargeQuery {

    public static ObservableList<Integer> getChargeOList() {

        try {

            ObservableList<Integer> chargeOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Charge_Amount FROM Charge");

            statement.close();

            while (resultSet.next())
                chargeOList.add(resultSet.getInt(1));

            return chargeOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean addCharge(Integer charge) {

        try {

            String insertQuery = "INSERT INTO Charge(Charge_Amount) VALUES(?)";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(insertQuery);

            preparedStatement.setInt(1, charge);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean removeCharge(Integer charge) {

        try {

            String deleteQuery = "DELETE FROM Charge WHERE Charge_Amount = ?";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(deleteQuery);

            preparedStatement.setInt(1, charge);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}