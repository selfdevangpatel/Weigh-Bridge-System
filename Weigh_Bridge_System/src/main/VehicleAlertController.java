package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import database.query.VehicleAlertQuery;

import main.record.VehicleAlertRecord;

public class VehicleAlertController {

    @FXML private AnchorPane anchorPane;
    @FXML private TextField vehicleNumberTF;
    @FXML private TextArea noteTA;
    @FXML private Button addVehicleBTN, removeVehicleBTN;
    @FXML private TableView<VehicleAlertRecord> vehicleAlertTV;
    @FXML private TableColumn<VehicleAlertRecord, String> vehicleNumberTCOL, noteTCOL;

    Stage vehicleAlertStage;

    VehicleAlertController() {

        FXMLLoader fxmlLoader = new FXMLLoader(VehicleAlertController.class.
                                               getResource("fxml/manage-vehicle-alert-view.fxml"));
        fxmlLoader.setController(this);

        Stage stage = new Stage();
        vehicleAlertStage = stage;

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        vehicleNumberTF.textProperty().addListener((observableValue, s, t1) ->
                        vehicleNumberTF.setText(t1.toUpperCase()));

        vehicleNumberTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().vehicleNumber()));
        noteTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().note()));

        vehicleAlertTV.requestFocus();
        vehicleAlertTV.setItems(MainAppController.vehicleAlertOList);

        addVehicleBTN.setOnAction(actionEvent -> {

            String vehicleNumberToAdd = vehicleNumberTF.getText();
            String noteToAdd = noteTA.getText();

            if (noteToAdd.isBlank()) noteToAdd = null;

            if (MainAppController.vehicleAlertOList.stream().anyMatch(vehicleAlertRecord ->
                                  vehicleAlertRecord.vehicleNumber().equals(vehicleNumberToAdd))) {

                showAlert("Cannot Add Party", "Party '" + vehicleNumberToAdd + "'" + " already exists.",
                          Alert.AlertType.ERROR);
            }
            else if (!vehicleNumberToAdd.isBlank() && VehicleAlertQuery.addVehicle(vehicleNumberToAdd, noteToAdd)) {

                MainAppController.vehicleAlertOList.add(new VehicleAlertRecord(vehicleNumberToAdd, noteToAdd));

                showAlert("Vehicle Alert Added", "Vehicle '" + vehicleNumberToAdd + "'" + " added successfully.",
                          Alert.AlertType.INFORMATION);

                vehicleNumberTF.clear();
                noteTA.clear();
            }
        });

        removeVehicleBTN.setOnAction(actionEvent -> {

            VehicleAlertRecord vehicleAlertRecordToRemove = vehicleAlertTV.getSelectionModel().getSelectedItem();

            if (vehicleAlertRecordToRemove != null && VehicleAlertQuery.
                                                      removeVehicle(vehicleAlertRecordToRemove.vehicleNumber())) {

                MainAppController.vehicleAlertOList.remove(vehicleAlertRecordToRemove);
            }
        });

        if (MainAppController.darkTheme)
            anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/manage_vehicle_alert_view.css").
                                                          toExternalForm());

        stage.setTitle("Manage Vehicle Alert");
        stage.setResizable(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApp.mainStage);
        stage.show();
    }

    private void showAlert(String headerText, String contextText, Alert.AlertType alertType) {

        Alert alert = new Alert(alertType);

        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(vehicleAlertStage);
        alert.show();
    }
}