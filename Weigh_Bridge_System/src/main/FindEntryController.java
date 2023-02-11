package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import database.query.FindQuery;
import database.query.MainQuery;

import main.record.EntryRecord;

public class FindEntryController {

    @FXML private AnchorPane anchorPane;
    @FXML private CheckBox anyContainerNumberCKBOX, chargeCKBOX, containerNumberCKBOX, dateCKBOX, entryStatusCKBOX;
    @FXML private CheckBox grossWeightCKBOX, materialCKBOX, netWeightCKBOX, partyCKBOX, paymentModeCKBOX;
    @FXML private CheckBox tareWeightCKBOX, vehicleNumberCKBOX;
    @FXML private ChoiceBox<Integer> chargeCHBOX;
    @FXML private ChoiceBox<String> materialCHBOX, partyCHBOX;
    @FXML private TableColumn<EntryRecord, String> chargeTCOL, containerNumberTCOL, entryDateTCOL, entryTimeTCOL;
    @FXML private TableColumn<EntryRecord, String> grossDateTCOL, grossManualTCOL, grossTimeTCOL;
    @FXML private TableColumn<EntryRecord, String> grossWeightTCOL, insertOperatorTCOL, netWeightTCOL, partyTCOL;
    @FXML private TableColumn<EntryRecord, String> tareManualTCOL, tareTimeTCOL, tareWeightTCOL;
    @FXML private TableColumn<EntryRecord, String> updateOperatorTCOL, vehicleNumberTCOL, paymentModeTCOL;
    @FXML private TableColumn<EntryRecord, String> serialNumberTCOL, tareDateTCOL, materialTCOL;
    @FXML private Label clickToSortLBL, clickPlusLBL;
    @FXML private Button closeTableViewBTN, findBTN, usingLikeOperatorBTN;
    @FXML private TextField containerNumberTF, vehicleNumberTF;
    @FXML private DatePicker dateFromDP, dateToDP;
    @FXML private RadioButton nonePartyRB, onlyPartyRB, entryStatusCompleteRB, entryStatusIncompleteRB;
    @FXML private RadioButton paymentModeCashRB, paymentModeCreditRB, paymentModeUPIRB, selectedPartyRB;
    @FXML private ToggleGroup paymentModeTG;
    @FXML private Spinner<Integer> grossWeightFromSP, grossWeightToSP, netWeightFromSP, netWeightToSP;
    @FXML private Spinner<Integer> serialNumberFromSP, serialNumberToSP, tareWeightFromSP, tareWeightToSP;
    @FXML private TableView<EntryRecord> tableViewTV;

    public void initialize() {

        dateCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            dateFromDP.setDisable(!t1); dateToDP.setDisable(!t1);
        });

        partyCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            nonePartyRB.setDisable(!t1);
            onlyPartyRB.setDisable(!t1);
            selectedPartyRB.setDisable(!t1);
            selectedPartyRB.setSelected(false);
            nonePartyRB.setSelected(true);
        });

        materialCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> materialCHBOX.setDisable(!t1));

        selectedPartyRB.selectedProperty().addListener((observableValue, aBoolean, t1) ->
                        partyCHBOX.setDisable(!t1));

        vehicleNumberCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            vehicleNumberTF.setDisable(!t1);
            usingLikeOperatorBTN.setDisable(!t1);
        });

        vehicleNumberTF.textProperty().addListener((observableValue, s, t1) -> {

            if(t1 != null) vehicleNumberTF.setText(vehicleNumberTF.getText().toUpperCase());
        });

        paymentModeCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            paymentModeCashRB.setDisable(!t1); paymentModeCreditRB.setDisable(!t1);
            paymentModeUPIRB.setDisable(!t1);
        });

        chargeCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> chargeCHBOX.setDisable(!t1));

        containerNumberCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            anyContainerNumberCKBOX.setDisable(!containerNumberCKBOX.isSelected());

            anyContainerNumberCKBOX.setSelected(true);
        });

        anyContainerNumberCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) ->
                                containerNumberTF.setDisable(t1));

        containerNumberTF.textProperty().addListener((observableValue, s, t1) -> {

            if(t1 != null) containerNumberTF.setText(containerNumberTF.getText().toUpperCase());
        });

        entryStatusCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            entryStatusCompleteRB.setDisable(!t1);
            entryStatusIncompleteRB.setDisable(!t1);
        });

        grossWeightCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            grossWeightFromSP.setDisable(!t1); grossWeightToSP.setDisable(!t1);
        });

        tareWeightCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            tareWeightFromSP.setDisable(!t1); tareWeightToSP.setDisable(!t1);
        });

        netWeightCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            netWeightFromSP.setDisable(!t1); netWeightToSP.setDisable(!t1);
        });

        int lastSerialNo = MainQuery.getLastSerialNumber();
        lastSerialNo = lastSerialNo == 0 ? 999999 : lastSerialNo;

        serialNumberFromSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, 1, 1));
        serialNumberToSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, lastSerialNo,
                                                                                            1));

        dateFromDP.setValue(LocalDate.now()); dateToDP.setValue(LocalDate.now());
        partyCHBOX.setItems(MainAppController.partyOList);
        partyCHBOX.getSelectionModel().selectFirst();
        chargeCHBOX.setItems(MainAppController.chargeOList);
        chargeCHBOX.getSelectionModel().selectFirst();
        materialCHBOX.setItems(MainAppController.materialOList);
        materialCHBOX.getSelectionModel().selectFirst();

        grossWeightFromSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, 1, 1));
        grossWeightToSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, 999999, 1));
        tareWeightFromSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, 1, 1));
        tareWeightToSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, 999999, 1));
        netWeightFromSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, 1, 1));
        netWeightToSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, 999999, 1));

        serialNumberTCOL.setCellValueFactory(v -> new SimpleStringProperty(String.valueOf(v.getValue().
                                                                                            serialNumber())));

        vehicleNumberTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().vehicleNumber()));
        chargeTCOL.setCellValueFactory(v -> new SimpleStringProperty(String.valueOf(v.getValue().charge())));
        partyTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().party()));
        containerNumberTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().containerNumber()));
        paymentModeTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().paymentMode()));
        materialTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().material()));
        tareWeightTCOL.setCellValueFactory(v -> new SimpleStringProperty(String.valueOf(v.getValue().tareWeight())));
        tareDateTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().tareDate()));
        tareTimeTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().tareTime()));
        tareManualTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().tareManual()));
        grossWeightTCOL.setCellValueFactory(v -> new SimpleStringProperty(String.valueOf(v.getValue().grossWeight())));
        grossDateTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().grossDate()));
        grossTimeTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().grossTime()));
        grossManualTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().grossManual()));
        netWeightTCOL.setCellValueFactory(v -> new SimpleStringProperty(String.valueOf(v.getValue().netWeight())));
        entryDateTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().entryDate()));
        entryTimeTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().entryTime()));
        insertOperatorTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().insertOperator()));
        updateOperatorTCOL.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().updateOperator()));

        FindEntry.findEntryStage.setOnShown(windowEvent -> {

            if (MainAppController.darkTheme)
                anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/find_entry_view.css").
                                                              toExternalForm());
        });
    }

    @FXML void findBTNAction() {

        long startTime = System.currentTimeMillis();

        String[] findQuery = new String[11];

        if (dateCKBOX.isSelected()) {

            Date dateFrom = Date.valueOf(dateFromDP.getValue());
            Date dateTo = Date.valueOf(dateToDP.getValue());

            findQuery[0] = "(Entry_Date BETWEEN #" + dateFrom + "# AND #" + dateTo + "#)";
        }

        if (partyCKBOX.isSelected() && partyCHBOX.getSelectionModel().getSelectedItem() != null) {

            if (nonePartyRB.isSelected())  findQuery[1] = "(Party IS NULL)";
            else if (onlyPartyRB.isSelected())  findQuery[1] = "(Party IS NOT NULL)";
            else {

                String party = partyCHBOX.getSelectionModel().getSelectedItem();

                if (party != null) findQuery[1] = "(Party = '" + party + "')";
            }
        }

        if (vehicleNumberCKBOX.isSelected() && !vehicleNumberTF.getText().isBlank()) {

            String vehicleNumber = vehicleNumberTF.getText();

            if (vehicleNumber.startsWith("LIKE ")) findQuery[2] = "(Vehicle_Number " + vehicleNumber + ")";
            else findQuery[2] = "(Vehicle_Number = '" + vehicleNumber + "')";
        }

        if (paymentModeCKBOX.isSelected()) {

            String paymentMode;

            RadioButton paymentModeRB = (RadioButton) paymentModeTG.getSelectedToggle();
            paymentMode = paymentModeRB.getText();
            findQuery[3] = "(Payment_Mode = '" + paymentMode + "')";
        }

        if (chargeCKBOX.isSelected()) {

            findQuery[4] = "(Charge = " + chargeCHBOX.getSelectionModel().getSelectedItem() + ")";
        }

        if (materialCKBOX.isSelected()) {

            String material = materialCHBOX.getSelectionModel().getSelectedItem();

            if (material != null)
                findQuery[5] = "(Material = '" + material + "')";
        }

        if (containerNumberCKBOX.isSelected()) {

            if (anyContainerNumberCKBOX.isSelected()) findQuery[6] = "(Container_Number IS NOT NULL)";
            else if (!containerNumberTF.getText().isBlank()) findQuery[6] = "(Container_Number = '" +
                                                                            containerNumberTF.getText() + "')";
            else containerNumberCKBOX.setDisable(true);
        }

        if (entryStatusCKBOX.isSelected()) {

            findQuery[7] = entryStatusIncompleteRB.isSelected() ? "(Net_Weight IS NULL)" : "(Net_Weight IS NOT NULL)";
        }

        if (grossWeightCKBOX.isSelected()) {

            int grossWeightFrom = grossWeightFromSP.getValue();
            int grossWeightTo = grossWeightToSP.getValue();

            findQuery[8] = "(Gross_Weight >= " + grossWeightFrom + " AND Gross_Weight <= " + grossWeightTo + ")";
        }

        if (tareWeightCKBOX.isSelected()) {

            int tareWeightFrom = tareWeightFromSP.getValue();
            int tareWeightTo = tareWeightToSP.getValue();

            findQuery[9] = "(Tare_Weight >= " + tareWeightFrom + " AND Tare_Weight <= " + tareWeightTo + ")";
        }

        if (netWeightCKBOX.isSelected()) {

            int netWeightFrom = netWeightFromSP.getValue();
            int netWeightTo = netWeightToSP.getValue();

            findQuery[10] = "(Net_Weight >= " + netWeightFrom + " AND Net_Weight <= " + netWeightTo + ")";
        }

        int serialNoMin = serialNumberFromSP.getValue(), serialNoMax = serialNumberToSP.getValue();

        StringBuilder finalQuery = new StringBuilder("SELECT Serial_Number FROM Entry WHERE (Serial_Number >= " +
                                                     serialNoMin + " AND Serial_Number <= " + serialNoMax + ")");

        for (int i = 0; i < 11; i++)
            if (findQuery[i] != null) finalQuery.append(" AND ").append(findQuery[i]);

        ArrayList<Integer> serialNumberList = FindQuery.getSerialNumberList(String.valueOf(finalQuery));

        tableViewTV.getItems().clear();
        tableViewTV.setItems(FindQuery.getEntryRecordOList(serialNumberList));

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;

        showFindAlert(serialNumberList.size(), timeTaken);
    }

    @FXML void closeTableViewBTNAction() {

        tableViewTV.setDisable(true); tableViewTV.setVisible(false);
        clickToSortLBL.setVisible(false);
        clickPlusLBL.setVisible(false);
        closeTableViewBTN.setVisible(false); closeTableViewBTN.setDisable(true);
        findBTN.setDisable(false);
    }

    @FXML void usingLikeOperatorBTNAction() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("Using LIKE operator");
        alert.setContentText("""
                Usage Examples :-

                1) Match the beginning of a string:
                    Example: LIKE 'GJ%'
                    Output: GJ15XX..., GJ01Y..., etc.

                2) Match the end of a string:
                    Example: LIKE '%5020'
                    Output: GJ15XX5020, MH04K5020, etc.

                    and many more...""");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(FindEntry.findEntryStage);
        alert.show();
    }

    private void showFindAlert(int recordCount, double timeTaken) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setHeaderText("Find Entry Status");
        alert.setContentText(recordCount + " record(s) found.\n\nTime taken: " + timeTaken + " second(s)");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(FindEntry.findEntryStage);

        if (recordCount == 0) {

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.show();
        }
        else {

            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("View");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent()) if (result.get() == ButtonType.OK) {

                tableViewTV.setDisable(false); tableViewTV.setVisible(true);
                clickToSortLBL.setVisible(true);
                clickPlusLBL.setVisible(true);
                closeTableViewBTN.setDisable(false); closeTableViewBTN.setVisible(true);
                findBTN.setDisable(true);
            }
        }
    }
}