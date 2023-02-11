package login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;

import org.apache.commons.lang3.StringUtils;

import com.fazecast.jSerialComm.SerialPort;

import database.Connection;
import database.SerialPortSettings;
import database.UserAuthentication;

import main.MainApp;

import serial.communication.SerialPortConnection;

import static login.Login.loginStage;

public class LoginController {

    @FXML private TextField usernameTF;
    @FXML private PasswordField passwordPF;
    @FXML private Button loginBTN;

    boolean serialPortConnected = false;

    public void initialize() {

        String databaseURL = getDatabaseURL();

        loginStage.setOnShown(windowEvent -> {

            // Database connection
            if (databaseURL != null) {

                if (Connection.connectToDatabase(databaseURL)) {

                    Connection.databaseConnected = true;
                }
                else {

                    Connection.databaseConnected = false;
                    showAlert(Alert.AlertType.ERROR, "Database Connection", "Failed to connect to database.");
                    loginBTN.setDisable(true);
                }
            }
            else {

                showAlert(Alert.AlertType.ERROR, "Database Connection",
                          "Database URL is null or 'settings.txt' file is missing");
                loginBTN.setDisable(true);
            }

            // Serial port connection
            SerialPort[] serialPorts = SerialPort.getCommPorts();

            if (serialPorts.length != 0) {

                SerialPortConnection.setSerialPorts(serialPorts);

                int[] serialPortSettings = SerialPortSettings.getSerialPortSettings();

                if (SerialPortConnection.connectSerialPort(serialPortSettings[0], serialPortSettings[1],
                                                           serialPortSettings[2], serialPortSettings[3],
                                                           serialPortSettings[4])) {

                    SerialPortConnection.serialPortConnected = true;
                }
                else showAlert(Alert.AlertType.WARNING, "Serial Port", "Serial port not open. The application won't " +
                               "be able to get weight.");
            }
            else showAlert(Alert.AlertType.WARNING, "Serial Port", "Serial port not found. The application won't be " +
                           "able to get weight.");
        });

        loginStage.setOnCloseRequest(windowEvent -> {

            if (serialPortConnected)
                SerialPortConnection.closeSerialPort();

            if (Connection.databaseConnected)
                Connection.closeDatabaseConnection();
        });
    }

    @FXML private void onLoginBTNAction() {

        String username = usernameTF.getText();
        String password = passwordPF.getText();

        if (!username.isBlank() && !password.isBlank()) {

            if (UserAuthentication.userIsValid(username, password)) {

                usernameTF.clear();
                passwordPF.clear();

                new MainApp().start(new Stage());
                loginStage.close();
            }
            else showAlert(Alert.AlertType.ERROR, "User Authentication", "Cannot login - Incorrect username and/or " +
                           "password provided");
        }
    }

    private String getDatabaseURL() {

        try {

            String settingsFilePath = ""; // Eg. "C:/Java/Weigh-Bridge-System/settings.txt";

            File file = new File(settingsFilePath);

            String line;

            if (file.exists()) {

                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

                while ((line = bufferedReader.readLine()) != null) {

                    if (line.startsWith("DB_URL")) {

                        bufferedReader.close();
                        return StringUtils.substringBetween(line, "\"");
                    }
                }
            }
        }
        catch (IOException e) { throw new RuntimeException(e); }

        return null;
    }

    private void showAlert(Alert.AlertType alertType, String headerText, String contextText) {

        Alert alert = new Alert(alertType);

        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(loginStage);
        alert.show();
    }
}