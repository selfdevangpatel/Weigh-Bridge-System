package database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    public static java.sql.Connection connection;
    public static boolean databaseConnected;

    public static boolean connectToDatabase(String databaseURL) {

        try {

            String databasePassword = "admin";

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            connection = DriverManager.getConnection("jdbc:ucanaccess://" + databaseURL +
                                                     ";jackcessOpener=database.CryptCodecOpener", "", databasePassword);

            return true;
        }
        catch (ClassNotFoundException | SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean closeDatabaseConnection() {

        try {

            connection.close();

            return connection.isClosed();
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}