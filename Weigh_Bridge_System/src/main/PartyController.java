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

import database.query.PartyQuery;

import main.record.PartyRecord;

public class PartyController {

    @FXML private AnchorPane anchorPane;
    @FXML private TextField partyNameTF, contactNumberTF, emailAddressTF;
    @FXML private TextArea addressTA;
    @FXML private Button addPartyBTN, removePartyBTN;
    @FXML private TableView<PartyRecord> partyTV;
    @FXML private TableColumn<PartyRecord, String> partyNameTCOL, contactNumberTCOL, emailAddressTCOL, addressTCOL;

    Stage partyStage;
    ObservableList<PartyRecord> partyRecordOList;

    PartyController() {

        FXMLLoader fxmlLoader = new FXMLLoader(PartyController.class.getResource("fxml/manage-party-view.fxml"));
        fxmlLoader.setController(this);

        Stage stage = new Stage();
        partyStage = stage;

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        partyNameTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().partyName()));
        contactNumberTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().contactNumber()));
        emailAddressTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().emailAddress()));
        addressTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().address()));

        partyRecordOList = PartyQuery.getPartyRecordOList();
        partyTV.setItems(partyRecordOList);
        partyTV.requestFocus();

        addPartyBTN.setOnAction(actionEvent -> {

            String partyNameToAdd = partyNameTF.getText();
            String contactNumberToAdd = contactNumberTF.getText();
            String emailAddressToAdd = emailAddressTF.getText();
            String addressToAdd = addressTA.getText();

            if (contactNumberToAdd.isBlank()) contactNumberToAdd = null;
            if (emailAddressToAdd.isBlank()) emailAddressToAdd = null;
            if (addressToAdd.isBlank()) addressToAdd = null;

            if (partyRecordOList.stream().anyMatch(partyRecord ->
                                 partyRecord.partyName().equals(partyNameToAdd))) {

                showAlert("Cannot Add Party", "Party '" + partyNameToAdd + "'" + " already exists.",
                          Alert.AlertType.ERROR);
            }
            else if (!partyNameToAdd.isBlank() && PartyQuery.addParty(partyNameToAdd, contactNumberToAdd,
                                                                      emailAddressToAdd, addressToAdd)) {

                partyRecordOList.add(new PartyRecord(partyNameToAdd, contactNumberToAdd, emailAddressToAdd,
                                                     addressToAdd));

                MainAppController.partyOList.add(partyNameToAdd);

                showAlert("Party Added", "Party '" + partyNameToAdd + "'" + " added successfully.",
                          Alert.AlertType.INFORMATION);

                partyNameTF.clear();
                contactNumberTF.clear();
                emailAddressTF.clear();
                addressTA.clear();
            }
        });

        removePartyBTN.setOnAction(actionEvent -> {

            PartyRecord partyRecordToRemove = partyTV.getSelectionModel().getSelectedItem();

            if (partyRecordToRemove != null && PartyQuery.removeParty(partyRecordToRemove.partyName())) {

                partyRecordOList.remove(partyRecordToRemove);

                MainAppController.partyOList.remove(partyRecordToRemove.partyName());
            }
        });

        if (MainAppController.darkTheme)
            anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/manage_party_view.css").
                                                          toExternalForm());

        stage.setTitle("Manage Party");
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
        alert.initOwner(partyStage);
        alert.show();
    }
}