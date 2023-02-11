package database.query;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import database.Connection;

import main.record.PartyRecord;

public class PartyQuery {

    public static ObservableList<PartyRecord> getPartyRecordOList() {

        try {

            ObservableList<PartyRecord> partyRecordOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Party_Name, Contact_Number, Email_Address, Address " +
                                                         "FROM Party");

            statement.close();

            while (resultSet.next())
                partyRecordOList.add(new PartyRecord(resultSet.getString(1), resultSet.getString(2),
                                                     resultSet.getString(3), resultSet.getString(4)));

            return partyRecordOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean addParty(String partyName, String contactNumber, String emailAddress, String address) {

        try {

            String insertQuery = "INSERT INTO Party(Party_Name, Contact_Number, Email_Address, Address) VALUES" +
                                 "(?, ?, ?, ?)";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, partyName);
            preparedStatement.setString(2, contactNumber);
            preparedStatement.setString(3, emailAddress);
            preparedStatement.setString(4, address);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean removeParty(String partyName) {

        try {

            String deleteQuery = "DELETE FROM Party WHERE Party_Name = ?";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(deleteQuery);

            preparedStatement.setString(1, partyName);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static ObservableList<String> getPartyOList() {

        try {

            ObservableList<String> partyOList = FXCollections.observableArrayList();

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Party_Name FROM Party");

            statement.close();

            while (resultSet.next())
                partyOList.add(resultSet.getString(1));

            return partyOList;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}