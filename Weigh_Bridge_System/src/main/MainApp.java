package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage stage) {

        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("fxml/main-view.fxml"));

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        stage.setTitle("Weigh-Bridge System");
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}