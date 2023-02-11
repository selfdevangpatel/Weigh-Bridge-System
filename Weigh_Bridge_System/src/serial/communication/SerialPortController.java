package serial.communication;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

import main.MainApp;
import main.MainAppController;

public class SerialPortController {

    @FXML private AnchorPane anchorPane;
    @FXML private ChoiceBox<SerialPort> portCHBOX;
    @FXML private Spinner<Integer> dataBitsSP;
    @FXML private ChoiceBox<Integer> stopBitsCHBOX, parityCHBOX, baudRateCHBOX;
    @FXML private Label saveFailLBL, saveSuccessLBL1, saveSuccessLBL2, serialPortErrorLBL;
    @FXML private Button saveSettingsBTN;

    public SerialPortController() {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("fxml/serial-port-settings-view.fxml"));
        fxmlLoader.setController(this);

        Stage stage = new Stage();

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        if (SerialPortConnection.serialPortConnected) {

            portCHBOX.setItems(FXCollections.observableArrayList(SerialPortConnection.getSerialPorts()));
            portCHBOX.getSelectionModel().select(SerialPortConnection.selectedSerialPort);

            baudRateCHBOX.getItems().addAll(300, 600, 1200, 2400, 4800, 9600);
            SpinnerValueFactory.IntegerSpinnerValueFactory data_bits_spinner = new SpinnerValueFactory.
                                IntegerSpinnerValueFactory(1, 32, 8, 1);
            dataBitsSP.setValueFactory(data_bits_spinner);
            stopBitsCHBOX.getItems().addAll(SerialPort.ONE_STOP_BIT, SerialPort.ONE_POINT_FIVE_STOP_BITS,
                                     SerialPort.TWO_STOP_BITS);
            parityCHBOX.getItems().addAll(SerialPort.NO_PARITY, SerialPort.ODD_PARITY, SerialPort.EVEN_PARITY,
                                   SerialPort.MARK_PARITY, SerialPort.SPACE_PARITY);

            baudRateCHBOX.setValue(SerialPortConnection.selectedSerialPort.getBaudRate());
            data_bits_spinner.setValue(SerialPortConnection.selectedSerialPort.getNumDataBits());
            stopBitsCHBOX.setValue(SerialPortConnection.selectedSerialPort.getNumStopBits());
            parityCHBOX.setValue(SerialPortConnection.selectedSerialPort.getParity());

            saveSettingsBTN.setDisable(false);
            serialPortErrorLBL.setVisible(false);

            saveSettingsBTN.setOnAction(actionEvent -> {

                if (SerialPortConnection.saveSerialPortSettings(portCHBOX.getSelectionModel().getSelectedIndex(),
                                                                baudRateCHBOX.getSelectionModel().getSelectedItem(),
                                                                dataBitsSP.getValue(), stopBitsCHBOX.getValue(),
                                                                parityCHBOX.getValue())) {

                    saveFailLBL.setVisible(false);
                    saveSuccessLBL1.setVisible(true); saveSuccessLBL2.setVisible(true);
                }
                else {

                    saveFailLBL.setVisible(true);
                    saveSuccessLBL1.setVisible(false); saveSuccessLBL2.setVisible(false);
                }
            });
        }

        if (MainAppController.darkTheme)
            anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/serial_port_settings_view.css").
                                                          toExternalForm());

        stage.setTitle("Serial Port Settings");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApp.mainStage);
        stage.show();
    }
}