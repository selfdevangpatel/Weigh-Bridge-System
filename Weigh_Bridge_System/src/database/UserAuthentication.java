package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserAuthentication {

    public static boolean userIsValid(String username, String password) {

        try {

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Username, Password FROM User");

            statement.close();

            if (resultSet.next())
                return resultSet.getString(1).equals(username) && resultSet.getString(2).equals(password);

            return false;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}