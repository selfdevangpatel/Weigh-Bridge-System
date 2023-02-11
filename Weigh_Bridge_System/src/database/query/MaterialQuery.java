package database.query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.Connection;

public class MaterialQuery {

    public static ObservableList<String> getMaterialOList() {

        try {

            ObservableList<String> materialOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Material FROM Material");

            statement.close();

            while (resultSet.next())
                materialOList.add(resultSet.getString(1));

            return materialOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean addMaterial(String material) {

        try {

            String insertQuery = "INSERT INTO Material(Material) VALUES(?)";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, material);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean removeMaterial(String material) {

        try {

            String deleteQuery = "DELETE FROM Material WHERE Material = ?";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(deleteQuery);

            preparedStatement.setString(1, material);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}