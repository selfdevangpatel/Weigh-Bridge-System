package analytics;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import datetime.DateTime;

import database.query.AnalyticsQuery;

import main.MainApp;
import main.MainAppController;

public class AnalyticsController {

    @FXML private AnchorPane anchorPane;
    @FXML private ChoiceBox<String> categoryCHBOX;
    @FXML private ChoiceBox<Integer> yearCHBOX;
    @FXML private Label dateRangeLBL, dateFromLBL;
    @FXML private DatePicker dateFromDP, dateToDP;
    @FXML private LineChart<String, Integer> lineCHART;
    @FXML private CategoryAxis lineChartCA;
    @FXML private NumberAxis lineChartNA;
    @FXML private BarChart<String, Integer> barCHART;
    @FXML private PieChart pieCHART;
    @FXML private CategoryAxis barChartCA;
    @FXML private NumberAxis barChartNA;

    private String selectedNodeStyle;

    public void initialize() {

        lineChartNA.setLowerBound(0);
        barChartNA.setLowerBound(0);

        categoryCHBOX.getItems().add(0, "Total Charge Per Day");
        categoryCHBOX.getItems().add(1, "Month - Wise Total Charge Of 1 Year");
        categoryCHBOX.getItems().add(2, "Entry Count Per Day");
        categoryCHBOX.getItems().add(3, "Entry Count Of 1 Day (24 - Hours)");
        categoryCHBOX.getItems().add(4, "State - Wise Entry Count");
        categoryCHBOX.getItems().add(5, "Party - Wise Entry Count");
        categoryCHBOX.getItems().add(6, "Material - Wise Entry Count");
        categoryCHBOX.getItems().add(7, "Operator - Wise Entry Count");

        categoryCHBOX.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {

            int selectedIndex = t1.intValue();

            switch (selectedIndex) {

                case 0 -> {

                    lineCHART.setDisable(false); lineCHART.setVisible(true); lineCHART.getData().clear();
                    barCHART.setDisable(true); barCHART.setVisible(false);
                    pieCHART.setDisable(true); pieCHART.setVisible(false);
                    dateRangeLBL.setText("Date range :"); dateFromLBL.setText("From");
                    dateFromDP.setDisable(false); dateToDP.setDisable(false);
                    yearCHBOX.setDisable(true);
                    lineChartCA.setLabel("Date");
                    lineChartNA.setLabel("Total Charge");
                }

                case 1 -> {

                    lineCHART.setDisable(false); lineCHART.setVisible(true); lineCHART.getData().clear();
                    barCHART.setDisable(true); barCHART.setVisible(false);
                    pieCHART.setDisable(true); pieCHART.setVisible(false);
                    dateRangeLBL.setText("Date range :"); dateFromLBL.setText("From");
                    dateFromDP.setDisable(true); dateToDP.setDisable(true);
                    yearCHBOX.setDisable(false);
                    lineChartCA.setLabel("12 - Months");
                    lineChartNA.setLabel("Total Charge");
                }

                case 2 -> {

                    lineCHART.setDisable(true); lineCHART.setVisible(false);
                    barCHART.setDisable(false); barCHART.setVisible(true); barCHART.getData().clear();
                    pieCHART.setDisable(true); pieCHART.setVisible(false);
                    dateRangeLBL.setText("Date range :"); dateFromLBL.setText("From");
                    dateFromDP.setDisable(false); dateToDP.setDisable(false);
                    yearCHBOX.setDisable(true);
                    barChartCA.setLabel("Date");
                    barChartNA.setLabel("Entry Count");
                }

                case 3 -> {

                    lineCHART.setDisable(true); lineCHART.setVisible(false);
                    barCHART.setDisable(false); barCHART.setVisible(true); barCHART.getData().clear();
                    pieCHART.setDisable(true); pieCHART.setVisible(false);
                    dateRangeLBL.setText("Date :"); dateRangeLBL.setAlignment(Pos.CENTER_RIGHT);
                    dateFromLBL.setText("Day");
                    dateFromDP.setDisable(false); dateToDP.setDisable(true);
                    yearCHBOX.setDisable(true);
                    barChartCA.setLabel("24 - Hours");
                    barChartNA.setLabel("Entry Count");
                }

                case 4, 5, 6 -> {

                    lineCHART.setDisable(true); lineCHART.setVisible(false);
                    barCHART.setDisable(true); barCHART.setVisible(false);
                    pieCHART.setDisable(false); pieCHART.setVisible(true); pieCHART.getData().clear();
                    dateRangeLBL.setText("Date range :"); dateFromLBL.setText("From");
                    yearCHBOX.setDisable(true);
                    dateFromDP.setDisable(false); dateToDP.setDisable(false);
                }

                case 7 -> {

                    lineCHART.setDisable(true); lineCHART.setVisible(false);
                    barCHART.setDisable(false); barCHART.setVisible(true); barCHART.getData().clear();
                    pieCHART.setDisable(true); pieCHART.setVisible(false);
                    dateRangeLBL.setText("Date :"); dateRangeLBL.setAlignment(Pos.CENTER_RIGHT);
                    dateFromLBL.setText("Day");
                    dateFromDP.setDisable(false); dateToDP.setDisable(true);
                    yearCHBOX.setDisable(true);
                    barChartCA.setLabel("Operator");
                    barChartNA.setLabel("Entry Count");
                }
            }
        });

        categoryCHBOX.getSelectionModel().selectFirst();

        dateFromDP.setValue(LocalDate.now());
        dateToDP.setValue(LocalDate.now());

        int year = LocalDate.now().getYear(), index = 0;

        for (int i = 2010; i <= year; i++) {

            yearCHBOX.getItems().add(index, i);
            index++;
        }

        yearCHBOX.getSelectionModel().selectLast();

        String lightNodeStyle = "-fx-font-weight: bold; -fx-background-color: white; -fx-border-color: black; " +
                                "-fx-border-radius: 3;  -fx-text-fill: black; -fx-padding: 0 2 0 2";
        String darkNodeStyle = "-fx-font-weight: bold; -fx-background-color: black; -fx-border-color: white; " +
                               "-fx-border-radius: 3;  -fx-text-fill: white; -fx-padding: 0 2 0 2";

        Analytics.analyticsStage.setOnShown(windowEvent -> {

            if (MainAppController.darkTheme) {

                anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/analytics_view.css").
                                                              toExternalForm());
                selectedNodeStyle = darkNodeStyle;
            }
            else selectedNodeStyle = lightNodeStyle;
        });
    }

    @FXML public void plotChartBTNAction() {

        int selectedIndex = categoryCHBOX.getSelectionModel().getSelectedIndex();

        if (selectedIndex == 0) {

            lineCHART.getData().clear();

            LocalDate dateFrom = dateFromDP.getValue();
            LocalDate dateTo = dateToDP.getValue();
            long dayCount = ChronoUnit.DAYS.between(dateFrom, dateTo) + 1;

            if (dayCount > 31) {

                showAlert(Alert.AlertType.ERROR, "Date Range Not Permissible",
                          "Please specify date range of <= 31 days");

                return ;
            }

            Map<Date, Integer> dateChargeMap = new TreeMap<>(AnalyticsQuery.getTotalChargeByDate(Date.valueOf(dateFrom),
                                                                                                 Date.valueOf(dateTo)));

            if (dateChargeMap.size() == 0) {

                showAlert(Alert.AlertType.INFORMATION, "Plot Chart Status", "No data found from " +
                          DateTime.dateToString(Date.valueOf(dateFrom)) + " to " +
                          DateTime.dateToString(Date.valueOf(dateTo)));

                return ;
            }

            XYChart.Series<String, Integer> series = new XYChart.Series<>();

            series.setName(categoryCHBOX.getSelectionModel().getSelectedItem());

            final Integer[] MAX_VALUE = {0};

            dateChargeMap.forEach((date, charge) -> {

                MAX_VALUE[0] = charge > MAX_VALUE[0] ? charge : MAX_VALUE[0];

                series.getData().add(new XYChart.Data<>(DateTime.dateToString(date), charge));
            });

            int[] numberAxisTick = getTickUnitAndUpperBound(MAX_VALUE[0]);

            lineChartNA.setTickUnit(numberAxisTick[0]);
            lineChartNA.setUpperBound(numberAxisTick[1]);

            series.getData().forEach(stringIntegerData -> {

                Integer yValue = stringIntegerData.getYValue();

                if (yValue != 0) stringIntegerData.setNode(createDataNodeForLineChart(yValue));
            });

            lineCHART.getData().add(series);
        }
        else if (selectedIndex == 1) {

            lineCHART.getData().clear();

            int selectedYear = yearCHBOX.getSelectionModel().getSelectedItem();

            Map<String, Integer> monthChargeMap = AnalyticsQuery.getMonthWiseTotalCharge(selectedYear);

            if (monthChargeMap.size() == 0) {

                showAlert(Alert.AlertType.INFORMATION, "Plot Chart Status", "No data found for year " + selectedYear);

                return ;
            }

            XYChart.Series<String, Integer> series = new XYChart.Series<>();

            series.setName(categoryCHBOX.getSelectionModel().getSelectedItem());

            final Integer[] MAX_VALUE = {0};

            monthChargeMap.forEach((month, charge) -> {

                MAX_VALUE[0] = charge > MAX_VALUE[0] ? charge : MAX_VALUE[0];

                series.getData().add(new XYChart.Data<>(month, charge));
            });

            int[] numberAxisTick = getTickUnitAndUpperBound(MAX_VALUE[0]);

            lineChartNA.setTickUnit(numberAxisTick[0]);
            lineChartNA.setUpperBound(numberAxisTick[1]);

            series.getData().forEach(stringIntegerData -> {

                Integer yValue = stringIntegerData.getYValue();

                if (yValue != 0) stringIntegerData.setNode(createDataNodeForLineChart(yValue));
            });

            lineCHART.getData().add(series);
        }
        else if (selectedIndex == 2) {

            barCHART.getData().clear();

            LocalDate dateFrom = dateFromDP.getValue();
            LocalDate dateTo = dateToDP.getValue();
            long dayCount = ChronoUnit.DAYS.between(dateFrom, dateTo) + 1;

            if (dayCount > 31) {

                showAlert(Alert.AlertType.ERROR, "Date Range Not Permissible",
                          "Please specify date range of <= 31 days");

                return ;
            }

            Map<Date, Long> dateEntryCountMap = new TreeMap<>(AnalyticsQuery.getEntryCountByDate(Date.valueOf(dateFrom),
                                                                                                 Date.valueOf(dateTo)));

            if (dateEntryCountMap.size() == 0) {

                showAlert(Alert.AlertType.INFORMATION, "Plot Chart Status", "No data found from " +
                          DateTime.dateToString(Date.valueOf(dateFrom)) + " to " +
                          DateTime.dateToString(Date.valueOf(dateTo)));

                return ;
            }

            XYChart.Series<String, Integer> series = new XYChart.Series<>();

            series.setName(categoryCHBOX.getSelectionModel().getSelectedItem());

            final Long[] MAX_VALUE = {0L};

            dateEntryCountMap.forEach((date, entryCount) -> {

                MAX_VALUE[0] = entryCount > MAX_VALUE[0] ? entryCount : MAX_VALUE[0];

                series.getData().add(new XYChart.Data<>(DateTime.dateToString(date), entryCount.intValue()));
            });

            int[] numberAxisTick = getTickUnitAndUpperBound(Math.toIntExact(MAX_VALUE[0]));

            barChartNA.setTickUnit(numberAxisTick[0]);
            barChartNA.setUpperBound(numberAxisTick[1]);

            series.getData().forEach(stringIntegerData -> {

                Integer yValue = stringIntegerData.getYValue();

                if (yValue != 0) stringIntegerData.setNode(createDataNodeForBarChart(yValue));
            });

            barCHART.getData().add(series);
        }
        else if (selectedIndex == 3) {

            barCHART.getData().clear();

            Date day = Date.valueOf(dateFromDP.getValue());

            Object[] entryCount24HourObj = AnalyticsQuery.getEntryCountBy24Hours(day);
            String[] hourNames = (String[]) entryCount24HourObj[0];
            int[] timeCount = (int[]) entryCount24HourObj[1];

            if (Arrays.stream(timeCount).allMatch(i -> i == 0)) {

                showAlert(Alert.AlertType.INFORMATION, "Plot Chart Status", "No data found for " +
                          DateTime.dateToString(day));

                return ;
            }

            XYChart.Series<String, Integer> series = new XYChart.Series<>();

            series.setName(categoryCHBOX.getSelectionModel().getSelectedItem());

            int maxValue = 0;

            for (int i = 0; i < hourNames.length; i++) {

                maxValue = Math.max(timeCount[i], maxValue);

                series.getData().add(new XYChart.Data<>(hourNames[i], timeCount[i]));
            }

            int[] numberAxisTick = getTickUnitAndUpperBound(maxValue);

            barChartNA.setTickUnit(numberAxisTick[0]);
            barChartNA.setUpperBound(numberAxisTick[1]);

            series.getData().forEach(stringIntegerData -> {

                Integer yValue = stringIntegerData.getYValue();

                if (yValue != 0) stringIntegerData.setNode(createDataNodeForBarChart(yValue));
            });

            barChartCA.setLabel("24 - Hours (" + DateTime.dateToString(Date.valueOf(dateFromDP.getValue())) + ")");
            barCHART.getData().add(series);
        }
        else if (selectedIndex == 4) {

            pieCHART.getData().clear();

            LocalDate dateFrom = dateFromDP.getValue();
            LocalDate dateTo = dateToDP.getValue();

            Map<String, Long> stateCountMap = AnalyticsQuery.getStateWiseEntryCount(Date.valueOf(dateFrom),
                                                                                    Date.valueOf(dateTo));

            if (stateCountMap.size() == 0) {

                showAlert(Alert.AlertType.INFORMATION, "Plot Chart Status", "No data found from " +
                          DateTime.dateToString(Date.valueOf(dateFrom)) + " to " +
                          DateTime.dateToString(Date.valueOf(dateTo)));

                return ;
            }

            stateCountMap.forEach((s, aLong) -> pieCHART.getData().add(new PieChart.Data(s + " (" + aLong + ")",
                                                                                         aLong)));

            pieCHART.setLegendVisible(false);
        }
        else if (selectedIndex == 5) {

            pieCHART.getData().clear();

            LocalDate dateFrom = dateFromDP.getValue();
            LocalDate dateTo = dateToDP.getValue();

            Map<String, Long> partyCountMap = AnalyticsQuery.getPartyEntryCount(Date.valueOf(dateFrom),
                                                                                Date.valueOf(dateTo));

            if (partyCountMap.size() == 0) {

                showAlert(Alert.AlertType.INFORMATION, "Plot Chart Status", "No data found from " +
                          DateTime.dateToString(Date.valueOf(dateFrom)) + " to " +
                          DateTime.dateToString(Date.valueOf(dateTo)));

                return ;
            }

            partyCountMap.forEach((s, aLong) -> pieCHART.getData().add(new PieChart.Data(s + " (" + aLong + ")",
                                                                                         aLong)));

            pieCHART.setLegendVisible(false);
        }
        else if (selectedIndex == 6) {

            pieCHART.getData().clear();

            LocalDate dateFrom = dateFromDP.getValue();
            LocalDate dateTo = dateToDP.getValue();

            Map<String, Long> materialEntryCountMap = AnalyticsQuery.getMaterialEntryCount(Date.valueOf(dateFrom),
                                                                                           Date.valueOf(dateTo));

            if (materialEntryCountMap.size() == 0) {

                showAlert(Alert.AlertType.INFORMATION, "Plot Chart Status", "No data found from " +
                          DateTime.dateToString(Date.valueOf(dateFrom)) + " to " +
                          DateTime.dateToString(Date.valueOf(dateTo)));

                return ;
            }

            materialEntryCountMap.forEach((s, aLong) -> pieCHART.getData().add(new PieChart.Data(s + " (" + aLong + ")",
                                                                                                 aLong)));

            pieCHART.setLegendVisible(false);
        }
        else if (selectedIndex == 7) {

            barCHART.getData().clear();

            Date day = Date.valueOf(dateFromDP.getValue());
            Object[] operatorEntryCountMap = AnalyticsQuery.getOperatorWiseEntryCount(day);

            Map<String, Long> insertOperatorMap = (Map<String, Long>) operatorEntryCountMap[0];
            Map<String, Long> updateOperatorMap = (Map<String, Long>) operatorEntryCountMap[1];

            if (insertOperatorMap.size() == 0 && updateOperatorMap.size() == 0) {

                showAlert(Alert.AlertType.INFORMATION, "Plot Chart Status", "No data found for " +
                          DateTime.dateToString(day));

                return ;
            }

            XYChart.Series<String, Integer> insertOperatorSeries = new XYChart.Series<>();

            insertOperatorSeries.setName("Insert Entry Count");

            XYChart.Series<String, Integer> updateOperatorSeries = new XYChart.Series<>();

            updateOperatorSeries.setName("Update Entry Count");

            final Long[] MAX_VALUE = {0L, 0L};

            insertOperatorMap.forEach(((s, aLong) -> {

                MAX_VALUE[0] = aLong > MAX_VALUE[0] ? aLong : MAX_VALUE[0];

                insertOperatorSeries.getData().add(new XYChart.Data<>(s, aLong.intValue()));
            }));

            updateOperatorMap.forEach(((s, aLong) -> {

                MAX_VALUE[1] = aLong > MAX_VALUE[1] ? aLong : MAX_VALUE[1];

                updateOperatorSeries.getData().add(new XYChart.Data<>(s, aLong.intValue()));
            }));

            Long greaterValue = MAX_VALUE[0] > MAX_VALUE[1] ? MAX_VALUE[0] : MAX_VALUE[1];
            int[] numberAxisTick = getTickUnitAndUpperBound(Math.toIntExact(greaterValue));

            barChartNA.setTickUnit(numberAxisTick[0]);
            barChartNA.setUpperBound(numberAxisTick[1]);

            insertOperatorSeries.getData().forEach(stringIntegerData -> {

                Integer yValue = stringIntegerData.getYValue();

                if (yValue != 0) stringIntegerData.setNode(createDataNodeForBarChart(yValue));
            });

            updateOperatorSeries.getData().forEach(stringIntegerData -> {

                Integer yValue = stringIntegerData.getYValue();

                if (yValue != 0) stringIntegerData.setNode(createDataNodeForBarChart(yValue));
            });

            barCHART.getData().add(insertOperatorSeries);
            barCHART.getData().add(updateOperatorSeries);
        }
    }

    private Node createDataNodeForLineChart(Integer value) {

        var label = new Label();

        label.setText(value.toString());

        var pane = new Pane(label);

        pane.setShape(new Circle(5.0));
        pane.setScaleShape(false);

        label.translateYProperty().bind(label.heightProperty().divide(-1.5));
        label.setStyle(selectedNodeStyle);

        return pane;
    }

    private Node createDataNodeForBarChart(Integer value) {

        var label = new Label();

        label.setText(value.toString());
        label.setStyle(selectedNodeStyle);
        label.translateYProperty().bind(label.heightProperty().divide(-1));

        return new Pane(label);
    }

    private void showAlert(Alert.AlertType alertType, String headerText, String contextText) {

        Alert alert = new Alert(alertType);

        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Analytics.analyticsStage);
        alert.show();
    }

    private int[] getTickUnitAndUpperBound(int maxValue) {

        int tickUnit = maxValue / 10;

        if (tickUnit == 0) tickUnit = 1;

        while (tickUnit % 5 != 0) tickUnit++;

        int upperBound = 0;

        while (upperBound <= maxValue) upperBound += tickUnit;

        upperBound += tickUnit;

        return new int[]{tickUnit, upperBound};
    }
}