package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutController {

    @FXML private AnchorPane anchorPane;
    @FXML private Hyperlink developerHLINK, uCanAccessHLINK, jSerialCommHLINK;
    @FXML private Hyperlink apachePDFBoxHLINK, apachePOIHLINK, jUniqueHLINK;

    AboutController() {

        FXMLLoader fxmlLoader = new FXMLLoader(AboutController.class.getResource("fxml/about-view.fxml"));
        fxmlLoader.setController(this);

        Stage stage = new Stage();

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        developerHLINK.setOnAction(actionEvent -> {

            try { Desktop.getDesktop().browse(new URI("https://github.com/selfdevangpatel")); }
            catch (IOException | URISyntaxException ignored) {}
        });

        uCanAccessHLINK.setOnAction(actionEvent -> {

            try { Desktop.getDesktop().browse(new URI("https://ucanaccess.sourceforge.net/site.html")); }
            catch (IOException | URISyntaxException ignored) {}
        });

        jSerialCommHLINK.setOnAction(actionEvent -> {

            try { Desktop.getDesktop().browse(new URI("https://fazecast.github.io/jSerialComm/")); }
            catch (IOException | URISyntaxException ignored) {}
        });

        apachePDFBoxHLINK.setOnAction(actionEvent -> {

            try { Desktop.getDesktop().browse(new URI("https://pdfbox.apache.org/")); }
            catch (IOException | URISyntaxException ignored) {}
        });

        apachePOIHLINK.setOnAction(actionEvent -> {

            try { Desktop.getDesktop().browse(new URI("https://poi.apache.org/")); }
            catch (IOException | URISyntaxException ignored) {}
        });

        jUniqueHLINK.setOnAction(actionEvent -> {

            try { Desktop.getDesktop().browse(new URI("http://www.sauronsoftware.it/projects/junique/")); }
            catch (IOException | URISyntaxException ignored) {}
        });

        if (MainAppController.darkTheme)
            anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/about_view.css").
                                                          toExternalForm());

        stage.setTitle("About Weigh-Bridge System");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApp.mainStage);
        stage.show();
    }
}