package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import database.query.OperatorQuery;

import main.record.OperatorRecord;

public class OperatorController {

    @FXML private AnchorPane anchorPane;
    @FXML private TextField operatorNameTF, contactNumberTF, emailAddressTF;
    @FXML private TextArea addressTA;
    @FXML private Button addOperatorBTN, removeOperatorBTN;
    @FXML private TableView<OperatorRecord> operatorTV;
    @FXML private TableColumn<OperatorRecord, String> operatorNameTCOL, contactNumberTCOL;
    @FXML private TableColumn<OperatorRecord, String> emailAddressTCOL, addressTCOL;

    Stage operatorStage;
    ObservableList<OperatorRecord> operatorRecordOList;

    OperatorController() {

        FXMLLoader fxmlLoader = new FXMLLoader(OperatorController.class.getResource("fxml/manage-operator-view.fxml"));
        fxmlLoader.setController(this);

        Stage stage = new Stage();
        operatorStage = stage;

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        operatorNameTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().operatorName()));
        contactNumberTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().contactNumber()));
        emailAddressTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().emailAddress()));
        addressTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().address()));

        operatorRecordOList = OperatorQuery.getOperatorRecordOList();
        operatorTV.setItems(operatorRecordOList);
        operatorTV.requestFocus();

        addOperatorBTN.setOnAction(actionEvent -> {

            String operatorNameToAdd = operatorNameTF.getText();
            String contactNumberToAdd = contactNumberTF.getText();
            String emailAddressToAdd = emailAddressTF.getText();
            String addressToAdd = addressTA.getText();

            if (contactNumberToAdd.isBlank()) contactNumberToAdd = null;
            if (emailAddressToAdd.isBlank()) emailAddressToAdd = null;
            if (addressToAdd.isBlank()) addressToAdd = null;

            if (operatorRecordOList.stream().anyMatch(operatorRecord ->
                                    operatorRecord.operatorName().equals(operatorNameToAdd))) {

                showAlert("Cannot Add Operator", "Operator '" + operatorNameToAdd + "'" + " already exists.",
                          Alert.AlertType.ERROR);
            }
            else if (!operatorNameToAdd.isBlank() && OperatorQuery.addOperator(operatorNameToAdd, contactNumberToAdd,
                                                                               emailAddressToAdd, addressToAdd)) {

                operatorRecordOList.add(new OperatorRecord(operatorNameToAdd, contactNumberToAdd,
                                                           emailAddressToAdd, addressToAdd));

                MainAppController.operatorOList.add(operatorNameToAdd);

                showAlert("Operator Added", "Operator '" + operatorNameToAdd + "'" + " added successfully.",
                          Alert.AlertType.INFORMATION);

                operatorNameTF.clear();
                contactNumberTF.clear();
                emailAddressTF.clear();
                addressTA.clear();
            }
        });

        removeOperatorBTN.setOnAction(actionEvent -> {

            OperatorRecord operatorRecordToRemove = operatorTV.getSelectionModel().getSelectedItem();

            if (operatorRecordToRemove != null && OperatorQuery.removeOperator(operatorRecordToRemove.operatorName())) {

                operatorRecordOList.remove(operatorRecordToRemove);

                MainAppController.operatorOList.remove(operatorRecordToRemove.operatorName());
            }
        });

        if (MainAppController.darkTheme)
            anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/manage_operator_view.css").
                                                          toExternalForm());

        stage.setTitle("Manage Operator");
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
        alert.initOwner(operatorStage);
        alert.show();
    }
}