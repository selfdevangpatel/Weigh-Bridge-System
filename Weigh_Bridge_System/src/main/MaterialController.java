package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import database.query.MaterialQuery;

public class MaterialController {

    @FXML private AnchorPane anchorPane;
    @FXML private TextField materialTF;
    @FXML private Button addMaterialBTN, removeMaterialBTN;
    @FXML private ListView<String> materialLV;

    private final Stage MATERIAL_STAGE;

    MaterialController() {

        FXMLLoader fxmlLoader = new FXMLLoader(MaterialController.class.getResource("fxml/manage-material-view.fxml"));
        fxmlLoader.setController(this);

        Stage stage = new Stage();
        MATERIAL_STAGE = stage;

        try { stage.setScene(new Scene(fxmlLoader.load())); }
        catch (IOException e) { throw new RuntimeException(e); }

        materialLV.requestFocus();
        materialLV.setItems(MainAppController.materialOList);

        addMaterialBTN.setOnAction(event -> {

            String materialToAdd = materialTF.getText();

            if (MainAppController.materialOList.contains(materialToAdd)) {

                showAlert("Cannot Add Material", "Material '" + materialToAdd + "'" + " already exists.",
                          Alert.AlertType.ERROR);
            }
            else if (materialToAdd != null && (MaterialQuery.addMaterial(materialToAdd))) {

                MainAppController.materialOList.add(materialToAdd);

                showAlert("Material Added", "Material '" + materialToAdd + "'" + " added successfully.",
                          Alert.AlertType.INFORMATION);

                materialTF.clear();
            }
        });

        removeMaterialBTN.setOnAction(event -> {

            String materialToRemove = materialLV.getSelectionModel().getSelectedItem();

            if (materialToRemove != null && MaterialQuery.removeMaterial(materialToRemove))
                MainAppController.materialOList.remove(materialToRemove);
        });

        if (MainAppController.darkTheme)
            anchorPane.getStylesheets().add(MainApp.class.getResource("dark_theme_css/manage_material_view.css").
                                                          toExternalForm());

        stage.setTitle("Manage Material");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApp.mainStage);
        stage.show();
    }

    private void showAlert(String headerText, String contextText, Alert.AlertType alertType) {

        Alert alert = new Alert(alertType);

        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MATERIAL_STAGE);
        alert.show();
    }
}