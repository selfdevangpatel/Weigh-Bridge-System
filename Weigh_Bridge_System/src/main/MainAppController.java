package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;

import analytics.Analytics;

import database.Connection;
import database.query.*;

import datetime.DateTime;

import print.Receipt;
import print.ReceiptRecord;

import report.Report;

import serial.communication.SerialPortConnection;
import serial.communication.SerialPortController;

import main.record.EntryFieldRecord;
import main.record.TareGrossWeightRecord;
import main.record.VehicleAlertRecord;

public class MainAppController {

    @FXML private AnchorPane anchorPane;
    @FXML private Label serialNumberLBL, tareManualLBL, grossManualLBL, scaleWeightLBL;
    @FXML private TextField serialNumberTF, vehicleNumberTF, containerNumberTF;
    @FXML private TextField tareWeightTF, tareDateTF, tareTimeTF, grossWeightTF, grossDateTF, grossTimeTF, netWeightTF;
    @FXML private RadioButton tareWeightRB, grossWeightRB, firstEntryRB, secondEntryRB;
    @FXML private RadioButton automaticWeightRB, manualWeightRB, cashRB, creditRB, UPIRB;
    @FXML private HBox mainPanelHBOX;
    @FXML private ComboBox<Integer> serialNumberCMBOX;
    @FXML private Button serialNumberSelectionModeBTN, editVehicleInfoBTN, clearPartySelectionBTN;
    @FXML private Button clearMaterialSelectionBTN, insertOrUpdateBTN;
    @FXML private ChoiceBox<Integer> chargeCHBOX;
    @FXML private ChoiceBox<String> partyCHBOX;
    @FXML private ChoiceBox<String> operatorCHBOX;
    @FXML private ChoiceBox<String> materialCHBOX;
    @FXML private ToggleGroup weightTypeTG, entryTypeTG, weightModeTG, paymentModeTG;
    @FXML private ImageView themeIV, serialNumberSelectionModeBTNIV;

    public static long loginMs = System.currentTimeMillis();

    private static Integer nextAutoIncrementID = MainQuery.getNextAutoIncrementID();
    private static boolean secondEntryEditMode;

    public static boolean darkTheme;
    private static final Image LIGHT_THEME_IMAGE = new Image(MainApp.class.
                                                                     getResourceAsStream("images/light_theme.png"));
    private static final Image DARK_THEME_IMAGE = new Image(MainApp.class.getResourceAsStream("images/dark_theme.png"));
    private static final Image EDIT_SN_IMAGE = new Image(MainApp.class.getResourceAsStream("images/editSN.png"));
    private static final Image SELECT_SN_IMAGE = new Image(MainApp.class.getResourceAsStream("images/selectSN.png"));

    static ObservableList<Integer> chargeOList = ChargeQuery.getChargeOList();
    static ObservableList<String> materialOList = MaterialQuery.getMaterialOList();
    public static ObservableList<String> partyOList = PartyQuery.getPartyOList();
    static ObservableList<String> operatorOList = OperatorQuery.getOperatorOList();
    static ObservableList<VehicleAlertRecord> vehicleAlertOList = VehicleAlertQuery.getVehicleAlertOList();

    ObservableList<Integer> tareEntryOList = MainQuery.getTareEntrySerialNoOList();
    ObservableList<Integer> grossEntryOList = MainQuery.getGrossEntrySerialNoOList();
    ObservableList<Integer> emptyOList = FXCollections.emptyObservableList();

    private static ListView listView;
    private static int sNCBSize;

    ScheduledExecutorService scaleWeightLBLUpdater;

    // Radio button CSS
    private static String unselectedRBCSS = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-text-fill: black";
    private static String selectedRBCSS = unselectedRBCSS + "; -fx-background-color: cyan";

    public void initialize() {

        serialNumberLBL.setText(Integer.toString(nextAutoIncrementID));

        setTareAndGrossDateTime();

        serialNumberCMBOX.getSelectionModel().selectedItemProperty().addListener((observableValue, integer, t1) -> {

            if (t1 != null) setEntryFields(t1);
            else clearEntryFields();
        });

        serialNumberTF.textProperty().addListener((observableValue, s, t1) -> {

            if (!t1.isBlank()) {

                int serialNumber = stringToInt(t1);

                if (serialNumber != 0 && MainQuery.serialNumberExists(serialNumber)) setEntryFields(serialNumber);
                else clearEntryFields();
            }
            else clearEntryFields();
        });

        chargeCHBOX.setItems(chargeOList);
        chargeCHBOX.getSelectionModel().selectFirst();

        partyCHBOX.setItems(partyOList);

        materialCHBOX.setItems(materialOList);

        operatorCHBOX.setItems(operatorOList);
        operatorCHBOX.getSelectionModel().selectFirst();

        // Weight Type Toggle Action
        weightTypeTG.selectedToggleProperty().addListener((observableValue, toggle, t1) -> setEntrySelection());

        // Entry Type Toggle Action
        entryTypeTG.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            if (firstEntryRB.isSelected()) {

                firstEntryRB.setStyle(selectedRBCSS);
                secondEntryRB.setStyle(unselectedRBCSS);

                weightModeTG.selectToggle(automaticWeightRB);

                mainPanelHBOX.setDisable(false);

                serialNumberLBL.setVisible(true);
                serialNumberTF.setVisible(false); serialNumberTF.setDisable(true);
                serialNumberCMBOX.setVisible(false); serialNumberCMBOX.setDisable(true);

                serialNumberLBL.setText(Integer.toString(nextAutoIncrementID));
                serialNumberSelectionModeBTN.setVisible(false); serialNumberSelectionModeBTN.setDisable(true);
                vehicleNumberTF.clear();
                editVehicleInfoBTN.setVisible(false); editVehicleInfoBTN.setDisable(true);
                chargeCHBOX.getSelectionModel().selectFirst();
                partyCHBOX.getSelectionModel().clearSelection();
                containerNumberTF.clear();
                paymentModeTG.selectToggle(cashRB);
                materialCHBOX.getSelectionModel().clearSelection();

                tareWeightTF.clear(); tareManualLBL.setVisible(false);
                grossWeightTF.clear(); grossManualLBL.setVisible(false);
                setTareAndGrossDateTime();
                netWeightTF.clear();

                disableVehicleInfoFields(false);

                insertOrUpdateBTN.setDisable(false); insertOrUpdateBTN.setText("Insert (F7)");

                mainPanelHBOX.setVisible(true);
            }
            else if (secondEntryRB.isSelected()) {

                firstEntryRB.setStyle(unselectedRBCSS);
                secondEntryRB.setStyle(selectedRBCSS);

                weightModeTG.selectToggle(automaticWeightRB);

                mainPanelHBOX.setDisable(false);

                serialNumberLBL.setVisible(false);

                serialNumberTF.setVisible(false); serialNumberTF.setDisable(true);
                serialNumberCMBOX.setDisable(false); serialNumberCMBOX.setVisible(true);

                serialNumberSelectionModeBTNIV.setImage(EDIT_SN_IMAGE);

                if (tareWeightRB.isSelected()) serialNumberCMBOX.setItems(grossEntryOList);
                else if (grossWeightRB.isSelected()) serialNumberCMBOX.setItems(tareEntryOList);

                serialNumberCMBOX.getSelectionModel().clearSelection();
                clearEntryFields();

                sNCBSize = serialNumberCMBOX.getItems().size();

                if (sNCBSize > 8) {

                    listView = (ListView) ((ComboBoxListViewSkin) serialNumberCMBOX.getSkin()).getPopupContent();

                    listView.scrollTo(sNCBSize - 1);
                }

                serialNumberSelectionModeBTN.setDisable(false); serialNumberSelectionModeBTN.setVisible(true);
                editVehicleInfoBTN.setDisable(false); editVehicleInfoBTN.setVisible(true);

                disableVehicleInfoFields(true);
                secondEntryEditMode = false;

                insertOrUpdateBTN.setDisable(false); insertOrUpdateBTN.setText("Update (F7)");

                mainPanelHBOX.setVisible(true);
            }
            else {

                firstEntryRB.setStyle(unselectedRBCSS);
                secondEntryRB.setStyle(unselectedRBCSS);
            }
        });

        // Weight Mode Toggle Action
        weightModeTG.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            if (t1.equals(automaticWeightRB)) {

                automaticWeightRB.setStyle(selectedRBCSS);
                manualWeightRB.setStyle(unselectedRBCSS);

                tareWeightTF.setEditable(false);
                tareDateTF.setEditable(false);
                tareTimeTF.setEditable(false);
                grossWeightTF.setEditable(false);
                grossDateTF.setEditable(false);
                grossTimeTF.setEditable(false);
            }
            else if (t1.equals(manualWeightRB)) {

                automaticWeightRB.setStyle(unselectedRBCSS);
                manualWeightRB.setStyle(selectedRBCSS);

                tareWeightTF.setEditable(true);
                tareDateTF.setEditable(true);
                tareTimeTF.setEditable(true);
                grossWeightTF.setEditable(true);
                grossDateTF.setEditable(true);
                grossTimeTF.setEditable(true);
            }
        });

        // Payment Mode Toggle Action
        paymentModeTG.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            if (t1.equals(cashRB)) {

                cashRB.setStyle(selectedRBCSS);
                creditRB.setStyle(unselectedRBCSS);
                UPIRB.setStyle(unselectedRBCSS);
            }
            else if (t1.equals(creditRB)) {

                cashRB.setStyle(unselectedRBCSS);
                creditRB.setStyle(selectedRBCSS);
                UPIRB.setStyle(unselectedRBCSS);
            }
            else if (t1.equals(UPIRB)) {

                cashRB.setStyle(unselectedRBCSS);
                creditRB.setStyle(unselectedRBCSS);
                UPIRB.setStyle(selectedRBCSS);
            }
        });

        vehicleNumberTF.textProperty().addListener((observableValue, s, t1) ->
                        vehicleNumberTF.setText(t1.toUpperCase()));

        partyCHBOX.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {

            if (firstEntryRB.isSelected() && t1 != null) paymentModeTG.selectToggle(creditRB);
        });

        containerNumberTF.textProperty().addListener((observableValue, s, t1) ->
                          containerNumberTF.setText(t1.toUpperCase()));

        if (SerialPortConnection.serialPortConnected) {

            scaleWeightLBLUpdater = Executors.newSingleThreadScheduledExecutor();

            scaleWeightLBLUpdater.scheduleAtFixedRate(() -> Platform.runLater(() -> {

                Integer scaleWeight = SerialPortConnection.getScaleWeight();

                if (scaleWeight != null) scaleWeightLBL.setText(scaleWeight.toString());
                else scaleWeightLBL.setText("");
            }), 0, 1200, TimeUnit.MILLISECONDS);
        }

        MainApp.mainStage.setOnShown(windowEvent -> MainApp.mainStage.getScene().setOnKeyPressed(keyEvent -> {

            switch (keyEvent.getCode()) {

                case F1 -> tareWeightRB.setSelected(true);
                case F2 -> grossWeightRB.setSelected(true);
                case F3 -> onPrintButtonAction();
                case F4 -> firstEntryRB.setSelected(true);
                case F5 -> secondEntryRB.setSelected(true);
                case F7 -> onInsertOrUpdateBTNAction();
                case F8 -> onGetWeightBTNAction();
            }
        }));

        MainApp.mainStage.setOnCloseRequest(windowEvent -> {

            windowEvent.consume();
            closeRequest(MainApp.mainStage);
        });

        tareWeightRB.setSelected(true);
        firstEntryRB.setSelected(true);
    }

    @FXML private void onSerialPortMIAction() { new SerialPortController(); }

    @FXML private void onUptimeMIAction() {

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss aa");
        Time loginTime = new Time(loginMs);

        long uptime = System.currentTimeMillis() - loginMs;
        long day = TimeUnit.MILLISECONDS.toDays(uptime);
        long hours = TimeUnit.MILLISECONDS.toHours(uptime) -
                     TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(uptime));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime) -
                       TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(uptime));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime) -
                       TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(uptime));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("Uptime - Time since login");
        alert.setContentText("Logged in at : " + simpleTimeFormat.format(loginTime).toUpperCase() + "\nUptime : " +
                             day + " Days " + hours + " Hours " + minutes + " Minutes " + seconds + " Seconds");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MainApp.mainStage);
        alert.show();
    }

    @FXML private void onChargeMIAction() { new ChargeController(); }
    @FXML private void onPartyMIAction() { new PartyController(); }
    @FXML private void onOperatorMIAction() { new OperatorController(); }
    @FXML private void onVehicleAlertMIAction() { new VehicleAlertController(); }
    @FXML private void onMaterialMIAction() { new MaterialController(); }
    @FXML private void onAboutMIAction() { new AboutController(); }
    @FXML private void onExitMIAction() { closeRequest(MainApp.mainStage); }

    @FXML private void setEntrySelection() {

        if (tareWeightRB.isSelected()) {

            tareWeightRB.setStyle(selectedRBCSS);
            grossWeightRB.setStyle(unselectedRBCSS);
        }
        else if (grossWeightRB.isSelected()) {

            tareWeightRB.setStyle(unselectedRBCSS);
            grossWeightRB.setStyle(selectedRBCSS);
        }

        if (entryTypeTG.getSelectedToggle() != null) entryTypeTG.getSelectedToggle().setSelected(false);

        weightModeTG.selectToggle(automaticWeightRB);

        mainPanelHBOX.setVisible(false);
        mainPanelHBOX.setDisable(true);
    }

    @FXML private void onFindBTNAction() { new FindEntry(); }

    @FXML private void onPrintButtonAction() {

        int serialNumber = 0;

        if (secondEntryRB.isSelected()) {

            if (serialNumberCMBOX.isVisible()) serialNumber = serialNumberCMBOX.getSelectionModel().getSelectedItem();
            else if (serialNumberTF.isVisible()) serialNumber = stringToInt(serialNumberTF.getText());
        }
        else if (firstEntryRB.isSelected()) serialNumber = stringToInt(serialNumberLBL.getText());

        if (MainQuery.serialNumberExists(serialNumber)) {

            ReceiptRecord receiptRecord = MainQuery.getReceiptRecord(serialNumber);

            if (receiptRecord != null) {

                PDDocument receiptDocument = Receipt.getReceiptDocument(receiptRecord);

                Receipt.createPrintReceiptJob(receiptDocument);
            }
        }
        else
            showAlert(Alert.AlertType.ERROR, "Print Entry Status", "Cannot print entry.\nSerial number " +
                      serialNumber + " does not exists.");
    }

    @FXML private void onReportBTNAction() { new Report(); }
    @FXML private void onAnalyticsBTNAction() { new Analytics(); }

    @FXML private void onThemeIVMouseEnteredAction() {

        darkTheme = !darkTheme;

        if (darkTheme) {

            themeIV.setImage(DARK_THEME_IMAGE);
            anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/main_view.css").toExternalForm());

            unselectedRBCSS = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-text-fill: white";
            selectedRBCSS = unselectedRBCSS + "; -fx-background-color: lime; -fx-text-fill: black";
        }
        else {

            themeIV.setImage(LIGHT_THEME_IMAGE);
            anchorPane.getStylesheets().clear();

            unselectedRBCSS = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-text-fill: black";
            selectedRBCSS = unselectedRBCSS + "; -fx-background-color: cyan";
        }

        tareWeightRB.setStyle(tareWeightRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
        grossWeightRB.setStyle(grossWeightRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
        firstEntryRB.setStyle(firstEntryRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
        secondEntryRB.setStyle(secondEntryRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
        automaticWeightRB.setStyle(automaticWeightRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
        manualWeightRB.setStyle(manualWeightRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
        cashRB.setStyle(cashRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
        creditRB.setStyle(creditRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
        UPIRB.setStyle(UPIRB.isSelected() ? selectedRBCSS : unselectedRBCSS);
    }

    @FXML private void onSerialNumberSelectionModeBTNAction() {

        if (serialNumberCMBOX.isVisible()) {

            Integer integer = serialNumberCMBOX.getSelectionModel().getSelectedItem();

            serialNumberCMBOX.setVisible(false); serialNumberCMBOX.setDisable(true);
            serialNumberTF.setDisable(false); serialNumberTF.setVisible(true);

            if (integer != null) serialNumberTF.setText(integer.toString());
            else {

                serialNumberTF.clear();
                serialNumberTF.requestFocus();
            }

            serialNumberSelectionModeBTNIV.setImage(SELECT_SN_IMAGE);
        }
        else if (serialNumberTF.isVisible()) {

            serialNumberTF.clear();

            serialNumberTF.setVisible(false); serialNumberTF.setDisable(true);
            serialNumberCMBOX.setDisable(false); serialNumberCMBOX.setVisible(true);
            serialNumberCMBOX.getSelectionModel().clearSelection();

            serialNumberSelectionModeBTNIV.setImage(EDIT_SN_IMAGE);
        }
    }

    @FXML private void onEditVehicleInfoBTNAction() {

        disableVehicleInfoFields(false);
        secondEntryEditMode = true;
    }

    @FXML private void onClearPartySelectionBTNAction() {

        partyCHBOX.getSelectionModel().clearSelection();
        if (firstEntryRB.isSelected()) paymentModeTG.selectToggle(cashRB);
    }

    @FXML private void onClearMaterialSelectionBTNAction() {

        materialCHBOX.getSelectionModel().clearSelection();
    }

    @FXML private void onDeleteBTNAction() {

        int serialNumber = 0;

        if (secondEntryRB.isSelected()) {

            if (serialNumberCMBOX.isVisible()) serialNumber = serialNumberCMBOX.getSelectionModel().getSelectedItem();
            else if (serialNumberTF.isVisible()) serialNumber = stringToInt(serialNumberTF.getText());
        }
        else if (firstEntryRB.isSelected()) serialNumber = stringToInt(serialNumberLBL.getText());

        int finalSerialNumber = serialNumber;

        if (MainQuery.serialNumberExists(serialNumber)) {

            Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);

            confirmDeleteAlert.setHeaderText("Confirm Delete");
            confirmDeleteAlert.setContentText("Delete entry with Serial No. '" + serialNumber + "' ?");
            confirmDeleteAlert.initModality(Modality.APPLICATION_MODAL);
            confirmDeleteAlert.initOwner(MainApp.mainStage);

            ((Button) confirmDeleteAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Delete");

            Optional<ButtonType> result = confirmDeleteAlert.showAndWait();

            if (result.isPresent()) if (result.get() == ButtonType.OK) {

                if (MainQuery.deleteEntry(serialNumber)) {

                    showAlert(Alert.AlertType.INFORMATION, "Entry Delete Status", "Entry with Serial No. '" +
                              serialNumber + "' successfully deleted.");

                    serialNumberCMBOX.getSelectionModel().clearSelection();
                    tareEntryOList.removeIf(integer -> integer.equals(finalSerialNumber));
                    grossEntryOList.removeIf(integer -> integer.equals(finalSerialNumber));
                    clearEntryFields();
                }
                else
                    showAlert(Alert.AlertType.ERROR, "Entry Delete Status", "Could not delete entry with Serial No. '" +
                              serialNumber + "'.");
            }
        }
        else
            showAlert(Alert.AlertType.ERROR, "Delete Entry Status", "Cannot delete entry.\nSerial No. '" +
                      serialNumber + "' does not exist.");
    }

    @FXML private void onInsertOrUpdateBTNAction() {

        long startTime = System.currentTimeMillis();

        boolean tareEntry = tareWeightRB.isSelected();

        // Vehicle number
        String vehicleNumber = vehicleNumberTF.getText();

        // Charge
        Integer charge = chargeCHBOX.getSelectionModel().getSelectedItem();

        // Party
        String party = partyCHBOX.getSelectionModel().getSelectedItem();

        // Container number
        String containerNumber = containerNumberTF.getText();

        if (containerNumber.isBlank()) containerNumber = null;

        // Payment mode
        String paymentMode = ((RadioButton) paymentModeTG.getSelectedToggle()).getText();

        // Material
        String material = materialCHBOX.getSelectionModel().getSelectedItem();

        int tareWeight = stringToInt(tareWeightTF.getText());
        int grossWeight = stringToInt(grossWeightTF.getText());
        int scaleWeight = stringToInt(scaleWeightLBL.getText());

        Date currentDate = DateTime.parseDate(DateTime.getCurrentDate());
        Time currentTime = DateTime.parseTime(DateTime.getCurrentTime());
        Date tareDate = null, grossDate = null;
        Time tareTime = null, grossTime = null;

        boolean validTareWeight = tareWeight != 0;
        boolean validGrossWeight = grossWeight != 0;

        if (validTareWeight && validGrossWeight) setNetWeight(tareWeight, grossWeight);
        int netWeight = stringToInt(netWeightTF.getText());

        String tareManual = null, grossManual = null;

        String operator = operatorCHBOX.getSelectionModel().getSelectedItem();

        if (firstEntryRB.isSelected()) {

            int serialNumber = stringToInt(serialNumberLBL.getText());

            if (validTareWeight) {

                tareDate = currentDate;
                tareTime = currentTime;
                tareManual = tareWeight == scaleWeight ? "No" : "Yes";
            }

            if (validGrossWeight) {

                grossDate = currentDate;
                grossTime = currentTime;
                grossManual = grossWeight == scaleWeight ? "No" : "Yes";
            }

            if (vehicleNumber.isBlank()) {

                showAlert(Alert.AlertType.ERROR, "Cannot Insert Entry", "Vehicle number is blank.");
                return ;
            }

            if (charge == null) {

                showAlert(Alert.AlertType.ERROR, "Cannot Insert Entry", "Charge is null.");
                return ;
            }

            if (tareEntry && !validTareWeight) {

                showAlert(Alert.AlertType.ERROR, "Cannot Insert Entry", "Tare weight is blank/zero/invalid.");
                return ;
            }
            else if (!tareEntry && !validGrossWeight) {

                showAlert(Alert.AlertType.ERROR, "Cannot Insert Entry", "Gross weight is blank/zero/invalid.");
                return ;
            }

            // Insert entry
            if (MainQuery.insertEntry(vehicleNumber, charge, party, paymentMode, containerNumber, material,
                                      tareWeight, tareDate, tareTime, tareManual, grossWeight, grossDate, grossTime,
                                      grossManual, netWeight, currentDate, currentTime, operator)) {

                MainQuery.setNextAutoIncrementID();
                nextAutoIncrementID = MainQuery.getNextAutoIncrementID();

                if (validTareWeight) {

                    tareDateTF.setText(DateTime.dateToString(tareDate));
                    tareTimeTF.setText(DateTime.timeToString(tareTime).toUpperCase());

                    tareManualLBL.setVisible(tareManual.equals("Yes"));

                    if (!validGrossWeight) tareEntryOList.add(serialNumber);
                }

                if (validGrossWeight) {

                    grossDateTF.setText(DateTime.dateToString(grossDate));
                    grossTimeTF.setText(DateTime.timeToString(grossTime).toUpperCase());

                    grossManualLBL.setVisible(grossManual.equals("Yes"));

                    if (!validTareWeight) grossEntryOList.add(serialNumber);
                }

                insertOrUpdateBTN.setDisable(true);

                long endTime = System.currentTimeMillis();
                double timeTaken = (endTime - startTime) / 1000.0;

                showPrintAlert("Insert Entry Status", "Entry successfully inserted.\nTime taken " + timeTaken +
                              " second(s)", serialNumber);
            }
            else
                showAlert(Alert.AlertType.ERROR, "Insert Entry Status", "Failed to insert entry.");
        }
        else if (secondEntryRB.isSelected()) {

            int serialNumber = 0;

            if (serialNumberCMBOX.isVisible()) serialNumber = serialNumberCMBOX.getSelectionModel().getSelectedItem();
            else if (serialNumberTF.isVisible()) serialNumber = stringToInt(serialNumberTF.getText());

            if (serialNumber == 0) return ;

            TareGrossWeightRecord tareGrossWeightRecord = MainQuery.getTareAndGrossWeight(serialNumber);

            if (tareGrossWeightRecord == null) return ;

            boolean tareUpdated = false, grossUpdated = false, vehicleInfoUpdated = false;

            if (validTareWeight && tareWeight != tareGrossWeightRecord.tareWeight()) {

                tareDate = currentDate;
                tareTime = currentTime;

                tareManual = tareWeight == scaleWeight ? "No" : "Yes";

                if (MainQuery.updateTareWeight(tareWeight, tareDate, tareTime, tareManual, netWeight, operator,
                                               serialNumber)) {

                    tareDateTF.setText(DateTime.dateToString(tareDate));
                    tareTimeTF.setText(DateTime.timeToString(tareTime).toUpperCase());

                    tareUpdated = true;
                }
                else
                    showAlert(Alert.AlertType.ERROR, "Update Entry Status", "Failed to update tare weight.");
            }

            if (validGrossWeight && grossWeight != tareGrossWeightRecord.grossWeight()) {

                grossDate = currentDate;
                grossTime = currentTime;

                grossManual = grossWeight == scaleWeight ? "No" : "Yes";

                if (MainQuery.updateGrossWeight(grossWeight, grossDate, grossTime, grossManual, netWeight, operator,
                                                serialNumber)) {

                    grossDateTF.setText(DateTime.dateToString(grossDate));
                    grossTimeTF.setText(DateTime.timeToString(grossTime).toUpperCase());

                    grossUpdated = true;
                }
                else
                    showAlert(Alert.AlertType.ERROR, "Update Entry Status", "Failed to update gross weight.");
            }

            if (secondEntryEditMode) {

                if (vehicleNumber.isBlank()) {

                    showAlert(Alert.AlertType.ERROR, "Cannot Update Entry", "Vehicle number is blank.");
                    return ;
                }

                if (charge == null) {

                    showAlert(Alert.AlertType.ERROR, "Cannot Update Entry", "Charge is null.");
                    return ;
                }

                if (MainQuery.updateVehicleInfo(vehicleNumber, charge, party, containerNumber, paymentMode, material,
                                                operator, serialNumber)) {

                    vehicleInfoUpdated = true;
                }
                else
                    showAlert(Alert.AlertType.ERROR, "Update Entry Status", "Failed to update vehicle info.");
            }

            String updateStatus = "";

            if (tareUpdated) updateStatus = "Tare weight info. updated.\n";
            if (grossUpdated) updateStatus = updateStatus + "Gross weight info. updated.\n";
            if (vehicleInfoUpdated) updateStatus = updateStatus + "Vehicle info. updated.\n";

            long endTime = System.currentTimeMillis();
            double timeTaken = (endTime - startTime) / 1000.0;

            updateStatus = updateStatus + "\nTime taken: " + timeTaken + " second(s)";

            showPrintAlert("Update Entry Status", updateStatus, serialNumber);

            // Remove Integer from OList when entry is completed.
            if (netWeight != 0) {

                Integer serialNumberToRemove = serialNumber;

                serialNumberCMBOX.setItems(emptyOList);

                if (tareEntry) {

                    grossEntryOList.remove(serialNumberToRemove);
                    serialNumberCMBOX.setItems(grossEntryOList);
                }
                else {

                    tareEntryOList.remove(serialNumberToRemove);
                    serialNumberCMBOX.setItems(tareEntryOList);
                }

                serialNumberCMBOX.setVisible(false); serialNumberCMBOX.setDisable(true);

                serialNumberTF.setDisable(false); serialNumberTF.setVisible(true);
                serialNumberTF.setText(serialNumberToRemove.toString());

                serialNumberSelectionModeBTNIV.setImage(SELECT_SN_IMAGE);
            }
        }

        for (VehicleAlertRecord vehicleAlertRecord : vehicleAlertOList) {

            if (vehicleAlertRecord.vehicleNumber().equals(vehicleNumber)) {

                String note = vehicleAlertRecord.note();

                showAlert(Alert.AlertType.INFORMATION, "Vehicle Alert", "Vehicle Alert contains '" +
                        vehicleAlertRecord.vehicleNumber() + "'" + (note == null ? "" : "\nNote: " + note));
            }
        }
    }

    @FXML private void onGetWeightBTNAction() {

        if (tareWeightRB.isSelected()) {

            tareWeightTF.setText(scaleWeightLBL.getText());
            setTareDateTime();
        }
        else {

            grossWeightTF.setText(scaleWeightLBL.getText());
            setGrossDateTime();
        }

        setNetWeight(stringToInt(tareWeightTF.getText()), stringToInt(grossWeightTF.getText()));
    }

    private void setTareDateTime() {

        tareDateTF.setText(DateTime.getCurrentDate());
        tareTimeTF.setText(DateTime.getCurrentTime().toUpperCase());
    }

    private void setGrossDateTime() {

        grossDateTF.setText(DateTime.getCurrentDate());
        grossTimeTF.setText(DateTime.getCurrentTime().toUpperCase());
    }

    private void setTareAndGrossDateTime() {

        String date = DateTime.getCurrentDate();
        String time = DateTime.getCurrentTime().toUpperCase();

        tareDateTF.setText(date);
        tareTimeTF.setText(time);
        grossDateTF.setText(date);
        grossTimeTF.setText(time);
    }

    private void disableVehicleInfoFields(boolean booleanValue) {

        vehicleNumberTF.setDisable(booleanValue);
        chargeCHBOX.setDisable(booleanValue); chargeCHBOX.setOpacity(1);
        partyCHBOX.setDisable(booleanValue); partyCHBOX.setOpacity(1);
        clearPartySelectionBTN.setDisable(booleanValue);
        containerNumberTF.setDisable(booleanValue);
        cashRB.setDisable(booleanValue); creditRB.setDisable(booleanValue); UPIRB.setDisable(booleanValue);
        materialCHBOX.setDisable(booleanValue); materialCHBOX.setOpacity(1);
        clearMaterialSelectionBTN.setDisable(booleanValue);
    }

    private void setEntryFields(int serialNumber) {

        EntryFieldRecord entryFieldRecord = MainQuery.getEntryFieldRecord(serialNumber);

        if (entryFieldRecord != null) {

            vehicleNumberTF.setText(entryFieldRecord.vehicleNumber());
            chargeCHBOX.getSelectionModel().select((Integer) entryFieldRecord.charge());
            partyCHBOX.getSelectionModel().select(entryFieldRecord.party());

            String containerNumber = entryFieldRecord.containerNumber();

            if (containerNumber != null) containerNumberTF.setText(containerNumber);
            else containerNumberTF.clear();

            String paymentMode = entryFieldRecord.paymentMode();

            switch (paymentMode) {

                case "Cash" -> paymentModeTG.selectToggle(cashRB);
                case "Credit" -> paymentModeTG.selectToggle(creditRB);
                case "UPI" -> paymentModeTG.selectToggle(UPIRB);
            }

            materialCHBOX.getSelectionModel().select(entryFieldRecord.material());

            int tareWeight = entryFieldRecord.tareWeight();
            int grossWeight = entryFieldRecord.grossWeight();
            int netWeight = entryFieldRecord.netWeight();

            if (tareWeight != 0) {

                tareWeightTF.setText(String.valueOf(tareWeight));
                tareDateTF.setText(DateTime.dateToString(entryFieldRecord.tareDate()));
                tareTimeTF.setText(DateTime.timeToString(entryFieldRecord.tareTime()).toUpperCase());
                tareManualLBL.setVisible(entryFieldRecord.tareManual().equals("Yes"));
            }
            else {

                tareWeightTF.clear();
                tareDateTF.setText(DateTime.getCurrentDate());
                tareTimeTF.setText(DateTime.getCurrentTime().toUpperCase());
                tareManualLBL.setVisible(false);
            }

            if (grossWeight != 0) {

                grossWeightTF.setText(String.valueOf(grossWeight));
                grossDateTF.setText(DateTime.dateToString(entryFieldRecord.grossDate()));
                grossTimeTF.setText(DateTime.timeToString(entryFieldRecord.grossTime()).toUpperCase());
                grossManualLBL.setVisible(entryFieldRecord.grossManual().equals("Yes"));
            }
            else {

                grossWeightTF.clear();
                grossDateTF.setText(DateTime.getCurrentDate());
                grossTimeTF.setText(DateTime.getCurrentTime().toUpperCase());
                grossManualLBL.setVisible(false);
            }

            if (netWeight != 0) netWeightTF.setText(String.valueOf(netWeight));
            else netWeightTF.clear();
        }
    }

    private void clearEntryFields() {

        vehicleNumberTF.clear();
        chargeCHBOX.getSelectionModel().clearSelection();
        partyCHBOX.getSelectionModel().clearSelection();

        containerNumberTF.clear();
        materialCHBOX.getSelectionModel().clearSelection();

        tareWeightTF.clear(); tareDateTF.clear(); tareTimeTF.clear(); tareManualLBL.setVisible(false);
        grossWeightTF.clear(); grossDateTF.clear(); grossTimeTF.clear(); grossManualLBL.setVisible(false);
        netWeightTF.clear();
    }

    private void setNetWeight(int tareWeight, int grossWeight) {

        if ((tareWeight != 0 && grossWeight != 0) && tareWeight != grossWeight)
            netWeightTF.setText(String.valueOf(grossWeight - tareWeight));
        else
            netWeightTF.clear();
    }

    private int stringToInt(String intString) {

        try { return Integer.parseInt(intString); }
        catch (NumberFormatException e) { return 0; }
    }

    private void showAlert(Alert.AlertType alertType, String headerText, String contextText) {

        Alert alert = new Alert(alertType);

        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MainApp.mainStage);
        alert.show();
    }

    private void showPrintAlert(String headerText, String contextText, int serialNumber) {

        Alert confirmPrintAlert = new Alert(Alert.AlertType.CONFIRMATION);

        confirmPrintAlert.setHeaderText(headerText);
        confirmPrintAlert.setContentText(contextText);
        confirmPrintAlert.initModality(Modality.APPLICATION_MODAL);
        confirmPrintAlert.initOwner(MainApp.mainStage);

        ((Button) confirmPrintAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Print");

        Optional<ButtonType> result = confirmPrintAlert.showAndWait();

        if (result.isPresent()) if (result.get() == ButtonType.OK) {

            ReceiptRecord receiptRecord = MainQuery.getReceiptRecord(serialNumber);

            if (receiptRecord != null) {

                PDDocument receiptDocument = Receipt.getReceiptDocument(receiptRecord);

                Receipt.createPrintReceiptJob(receiptDocument);
            }
        }
    }

    private void closeRequest(Stage stage) {

        Alert confirmCloseAlert = new Alert(Alert.AlertType.CONFIRMATION);

        confirmCloseAlert.setHeaderText("Confirm Exit");
        confirmCloseAlert.setContentText("Are you sure you want to exit?");
        confirmCloseAlert.initModality(Modality.APPLICATION_MODAL);
        confirmCloseAlert.initOwner(MainApp.mainStage);

        ((Button) confirmCloseAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Exit");

        Optional<ButtonType> result = confirmCloseAlert.showAndWait();

        if (result.isPresent()) {

            if (result.get() == ButtonType.OK) {

                if (SerialPortConnection.serialPortConnected) {

                    scaleWeightLBLUpdater.shutdownNow();
                    SerialPortConnection.closeSerialPort();
                }

                try { Connection.connection.close(); }
                catch (SQLException ignored) {}

                stage.close();
            }
        }
    }
}