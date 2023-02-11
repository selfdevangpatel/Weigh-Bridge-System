package report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import main.MainApp;

public class Report {

    static Stage reportStage;

    public Report () {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("fxml/generate-report-view.fxml"));

        Stage stage = new Stage();
        reportStage = stage;

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        stage.setTitle("Generate Report");
        stage.setResizable(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApp.mainStage);
        stage.show();
    }
}