package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;

public class Login extends Application {

    static Stage loginStage;

    @Override
    public void start(Stage stage) {

        loginStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("fxml/login-view.fxml"));

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        stage.setTitle("Weigh-Bridge System");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {

        String appId = "Weigh-Bridge-System";
        boolean alreadyRunning;

        try {

            JUnique.acquireLock(appId);
            alreadyRunning = false;
        }
        catch (AlreadyLockedException e) { alreadyRunning = true; }

        if (!alreadyRunning) launch();
        else System.exit(0);
    }
}