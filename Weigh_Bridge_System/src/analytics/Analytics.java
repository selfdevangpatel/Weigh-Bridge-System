package analytics;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import main.MainApp;

public class Analytics {

    static Stage analyticsStage;

    public Analytics() {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("fxml/analytics-view.fxml"));

        Stage stage = new Stage();
        analyticsStage = stage;

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        stage.setTitle("Analytics");
        stage.setResizable(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApp.mainStage);
        stage.show();
    }
}