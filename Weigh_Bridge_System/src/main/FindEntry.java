package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FindEntry {

    static Stage findEntryStage;

    FindEntry() {

        FXMLLoader fxmlLoader = new FXMLLoader(FindEntry.class.getResource("fxml/find-entry-view.fxml"));

        Stage stage = new Stage();
        findEntryStage = stage;

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        stage.setTitle("Find Entry");
        stage.setResizable(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApp.mainStage);
        stage.show();
    }
}