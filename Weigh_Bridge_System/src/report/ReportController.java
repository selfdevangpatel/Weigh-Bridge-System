package report;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;

import database.query.MainQuery;
import database.query.ReportQuery;

import datetime.DateTime;

import main.MainApp;
import main.MainAppController;

public class ReportController {

    @FXML private AnchorPane anchorPane;
    @FXML private Spinner<Integer> serialNumberFromSP, serialNumberToSP;
    @FXML private CheckBox anyVehicleNumberCKBOX;
    @FXML private DatePicker dateFromDP, dateToDP;
    @FXML private RadioButton anyPaymentModeRB, selectedPaymentModeRB;
    @FXML private RadioButton completeEntryStatusRB, incompleteEntryStatusRB, nonePartyRB;
    @FXML private RadioButton anyPartyRB, onlyPartyRB, selectedPartyRB, msExcelRB, pdfRB;
    @FXML private TextField vehicleNumberTF, exportLocationTF;
    @FXML private ChoiceBox<String> partyCHBOX, paymentModeCHBOX, reportFormatCHBOX;
    @FXML private ToggleGroup entryStatusTG;
    @FXML private Button printBTN, exportBTN;
    @FXML private Label reportGeneratedLBL, exportStatusLBL;

    private static PDDocument pdDocument;

    public void initialize() {

        int lastSerialNumber = MainQuery.getLastSerialNumber();
        lastSerialNumber = lastSerialNumber == 0 ? 999999 : lastSerialNumber;

        serialNumberFromSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, 1, 1));
        serialNumberToSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999999, lastSerialNumber,
                                                                                            1));

        dateFromDP.setValue(LocalDate.now()); dateToDP.setValue(LocalDate.now());

        anyVehicleNumberCKBOX.selectedProperty().addListener((observableValue, aBoolean, t1) ->
                              vehicleNumberTF.setDisable(t1));

        vehicleNumberTF.textProperty().addListener((observableValue, s, t1) ->
                        vehicleNumberTF.setText(t1.toUpperCase()));

        selectedPaymentModeRB.selectedProperty().addListener((observableValue, aBoolean, t1) ->
                              paymentModeCHBOX.setDisable(!t1));

        selectedPartyRB.selectedProperty().addListener((observableValue, aBoolean, t1) ->
                        partyCHBOX.setDisable(!t1));

        partyCHBOX.setItems(MainAppController.partyOList);
        partyCHBOX.getSelectionModel().selectFirst();

        paymentModeCHBOX.getItems().addAll("Cash", "Credit", "UPI");
        paymentModeCHBOX.getSelectionModel().selectFirst();

        reportFormatCHBOX.getItems().add(0, "Serial No. | Vehicle No. | Charges | Tare Weight | Gross Weight | " +
                                            "Net Weight");
        reportFormatCHBOX.getItems().add(1, "Serial No. | Vehicle No. | Charges | Tare Weight | Gross Weight | " +
                                            "Net Weight | Payment Mode");
        reportFormatCHBOX.getItems().add(2, "Serial No. | Vehicle No. | Charges | Tare Weight | Tare Date | Gross " +
                                            "Weight | Gross Date | Net Weight");
        reportFormatCHBOX.getItems().add(3, "Serial No. | Vehicle No. | Charges | Tare Weight | Tare Date | Gross " +
                                            "Weight | Gross Date | Net Weight | Payment Mode");
        reportFormatCHBOX.getItems().add(4, "Serial No. | Vehicle No. | Charges | Tare Weight | Tare Date | Tare " +
                                            "Time | Gross Weight | Gross Date | Gross Time | Net Weight");
        reportFormatCHBOX.getItems().add(5, "Serial No. | Vehicle No. | Charges | Tare Weight | Tare Date | Tare " +
                                            "Time | Gross Weight | Gross Date | Gross Time | Net Weight | Payment " +
                                            "Mode");
        reportFormatCHBOX.getSelectionModel().selectFirst();

        Report.reportStage.setOnShown(windowEvent -> {

            if (MainAppController.darkTheme)
                anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/generate_report_view.css").
                                                              toExternalForm());
        });

        Report.reportStage.setOnCloseRequest(windowEvent -> {

            try { if (pdDocument != null) pdDocument.close(); }
            catch (IOException e) { throw new RuntimeException(e); }
        });
    }

    @FXML private void onGenerateBTNAction() {

        try { if (pdDocument != null) pdDocument.close(); }
        catch (IOException e) { throw new RuntimeException(e); }

        String[] findQuery = new String[5];

        String vehicleNumber = vehicleNumberTF.getText();
        String paymentMode = paymentModeCHBOX.getSelectionModel().getSelectedItem();
        String entryStatus = ((RadioButton) entryStatusTG.getSelectedToggle()).getText();
        String party = partyCHBOX.getSelectionModel().getSelectedItem();

        Date dateFrom = Date.valueOf(dateFromDP.getValue());
        Date dateTo = Date.valueOf(dateToDP.getValue());
        findQuery[0] = "(Entry_Date BETWEEN #" + dateFrom + "# AND #" + dateTo + "#)";

        if (anyVehicleNumberCKBOX.isSelected()) vehicleNumber = "Any";
        else if (!anyVehicleNumberCKBOX.isSelected()) {

            if (!vehicleNumber.isBlank()) findQuery[1] = "(Vehicle_Number = '" + vehicleNumber + "')";
            else anyVehicleNumberCKBOX.setSelected(true);
        }

        if (selectedPaymentModeRB.isSelected()) findQuery[2] = "(Payment_Mode = '" + paymentMode + "')";
        else if (anyPaymentModeRB.isSelected()) paymentMode = "Any";

        if (completeEntryStatusRB.isSelected()) findQuery[4] = "(Net_Weight IS NOT NULL)";
        else if (incompleteEntryStatusRB.isSelected()) findQuery[4] = "(Net_Weight IS NULL)";

        if (nonePartyRB.isSelected()) {

            findQuery[3] = "Party IS NULL";
            party = "None";
        }
        else if (onlyPartyRB.isSelected()) {

            findQuery[3] = "Party IS NOT NULL";
            party = "Only";
        }
        else if (anyPartyRB.isSelected()) party = "Any";
        else if (selectedPartyRB.isSelected()) findQuery[3] = "Party = '" + party + "'";

        int serialNumberMin = serialNumberFromSP.getValue(), serialNumberMax = serialNumberToSP.getValue();

        StringBuilder finalQuery = new StringBuilder("SELECT Serial_Number FROM Entry WHERE (Serial_Number >= " +
                                                     serialNumberMin + " AND Serial_Number <= " + serialNumberMax +
                                                     ")");

        for (int i = 0; i < 5; i++)
            if (findQuery[i] != null) finalQuery.append(" AND ").append(findQuery[i]);

        ArrayList<Integer> serialNumberList = ReportQuery.getSerialNumberList(String.valueOf(finalQuery));
        int serialNumberListSize = serialNumberList.size();

        if (serialNumberListSize != 0) {

            ReportInfoRecord reportInfoRecord = new ReportInfoRecord(serialNumberMin, serialNumberMax,
                                                                     DateTime.dateToString(dateFrom),
                                                                     DateTime.dateToString(dateTo), vehicleNumber,
                                                                     paymentMode, entryStatus, party);

            pdDocument = ReportGenerator.createReport(reportInfoRecord, serialNumberList,
                                                      reportFormatCHBOX.getSelectionModel().getSelectedIndex(),
                                                      selectedPartyRB.isSelected());

            if (pdDocument != null) {

                printBTN.setDisable(false);

                reportGeneratedLBL.setText("Report generated with " + serialNumberListSize + " record" +
                                           (serialNumberListSize == 1 ? "." : "s."));
                reportGeneratedLBL.setStyle("-fx-text-fill: #00be00");

                exportLocationTF.clear();
                exportBTN.setDisable(false);
                exportStatusLBL.setText("");
            }
            else resetReportGenerateOptions();
        }
        else {

            resetReportGenerateOptions();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setHeaderText("Report Generate Status");
            alert.setContentText("0 records found.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(Report.reportStage);
            alert.show();
        }
    }

    @FXML private void onExportBTNAction() {

        String exportLocation = exportLocationTF.getText();

        if (pdfRB.isSelected()) {

            try {

                if (exportLocation.endsWith(".pdf")) pdDocument.save(exportLocation);
                else pdDocument.save(exportLocation + ".pdf");

                setExportStatusLBL(true, "PDF");
            }
            catch (IOException e) {

                setExportStatusLBL(false, "PDF");

                throw new RuntimeException(e);
            }
        }
        else if (msExcelRB.isSelected()) {

            if (!exportLocation.endsWith(".xlsx")) exportLocation = exportLocation + ".xlsx";

            setExportStatusLBL(ExcelReport.createExcelReport(exportLocation), "XLSX");
        }
    }

    @FXML private void onBrowseBTNAction() {

        try {

            FileChooser.ExtensionFilter extensionPdf = new FileChooser.ExtensionFilter("PDF (.pdf)", "*.pdf");
            FileChooser.ExtensionFilter extensionXslx = new FileChooser.ExtensionFilter("Excel (.xlsx)", "*.xlsx");

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Report Export Location");
            fileChooser.getExtensionFilters().add(pdfRB.isSelected() ? extensionPdf : extensionXslx);

            File report_file = fileChooser.showSaveDialog(Report.reportStage);

            if (report_file != null) exportLocationTF.setText(report_file.getAbsolutePath());
        }
        catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @FXML private void onPrintBTNAction() {

        if (pdDocument != null)
            print.Report.createPrintReportJob(pdDocument);
    }

    private void resetReportGenerateOptions() {

        printBTN.setDisable(true);

        reportGeneratedLBL.setText("");

        exportLocationTF.clear();
        exportBTN.setDisable(true);
        exportStatusLBL.setText("");
    }

    private void setExportStatusLBL(boolean success, String fileType) {

        if (success) {

            exportStatusLBL.setText(fileType + " successfully exported!");
            exportStatusLBL.setStyle("-fx-text-fill: #00be00");
        }
        else {

            exportStatusLBL.setText("Failed to export " + fileType);
            exportStatusLBL.setStyle("-fx-text-fill: red");
        }
    }
}