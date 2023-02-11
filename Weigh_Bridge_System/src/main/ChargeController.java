package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import database.query.ChargeQuery;

public class ChargeController {

    @FXML private AnchorPane anchorPane;
    @FXML private TextField chargeTF;
    @FXML private Button addChargeBTN, removeChargeBTN;
    @FXML private ListView<Integer> chargeLV;

    private final Stage CHARGE_STAGE;

    ChargeController() {

        FXMLLoader fxmlLoader = new FXMLLoader(ChargeController.class.getResource("fxml/manage-charge-view.fxml"));
        fxmlLoader.setController(this);

        Stage stage = new Stage();
        CHARGE_STAGE = stage;

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        chargeLV.requestFocus();
        chargeLV.setItems(MainAppController.chargeOList);

        addChargeBTN.setOnAction(event -> {

            Integer chargeToAdd = getInteger(chargeTF.getText());

            if (MainAppController.chargeOList.contains(chargeToAdd)) {

                showAlert("Cannot Add Charge", "Charge '" + chargeToAdd + "'" + " already exists.",
                          Alert.AlertType.ERROR);
            }
            else if (chargeToAdd != null && (ChargeQuery.addCharge(chargeToAdd))) {

                MainAppController.chargeOList.add(chargeToAdd);

                showAlert("Charge Added", "Charge '" + chargeToAdd + "'" + " added successfully.",
                          Alert.AlertType.INFORMATION);

                chargeTF.clear();
            }
        });

        removeChargeBTN.setOnAction(event -> {

            Integer chargeToRemove = chargeLV.getSelectionModel().getSelectedItem();

            if (chargeToRemove != null && ChargeQuery.removeCharge(chargeToRemove))
                MainAppController.chargeOList.remove(chargeToRemove);
        });

        if (MainAppController.darkTheme)
            anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/manage_charge_view.css").
                                                          toExternalForm());

        stage.setTitle("Manage charge");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApp.mainStage);
        stage.show();
    }

    private Integer getInteger(String intString) {

        try { return Integer.parseInt(intString); }
        catch (NumberFormatException nfe) { return null; }
    }

    private void showAlert(String headerText, String contextText, Alert.AlertType alertType) {

        Alert alert = new Alert(alertType);

        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(CHARGE_STAGE);
        alert.show();
    }
}