package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SerialPortSettings {

    public static int[] getSerialPortSettings() {

        try {

            int[] serialPortSettings = new int[5];

            Statement statement = Connection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Index, Baud_Rate, Data_Bits, Stop_Bits, Parity " +
                                                         "FROM SerialPort WHERE ID = 1");

            statement.close();

            if (resultSet.next()) {

                serialPortSettings[0] = resultSet.getInt(1);
                serialPortSettings[1] = resultSet.getInt(2);
                serialPortSettings[2] = resultSet.getInt(3);
                serialPortSettings[3] = resultSet.getInt(4);
                serialPortSettings[4] = resultSet.getInt(5);
            }

            return serialPortSettings;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean saveSerialPortSettings(int selectedPortIndex, int baudRate, int dataBits, int stopBits,
                                                 int parity) {

        try {

            String updateQuery = "UPDATE SerialPort SET Index = ?, Baud_Rate = ?, Data_Bits = ?, Stop_Bits = ?, " +
                                 "Parity = ? WHERE ID = 1";

            PreparedStatement preparedStatement = Connection.connection.prepareStatement(updateQuery);

            preparedStatement.setInt(1, selectedPortIndex);
            preparedStatement.setInt(2, baudRate);
            preparedStatement.setInt(3, dataBits);
            preparedStatement.setInt(4, stopBits);
            preparedStatement.setInt(5, parity);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }
}