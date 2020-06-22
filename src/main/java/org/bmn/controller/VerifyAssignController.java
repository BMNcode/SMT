package org.bmn.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

import static org.bmn.util.OutputResult.writeWorkbook;
import static org.bmn.util.VerifyAssign.verifyAssign;

public class VerifyAssignController {

    private File assignFile;
    private File specFile;
    Stage stage;

    public void verifyAssignEditor() {
        try {
            Stage stageImageEditor = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/verifyAssign.fxml"));
            Parent root = loader.load();
            stageImageEditor.setTitle("Verify Assign");
            stageImageEditor.setScene(new Scene(root));
            stageImageEditor.initModality(Modality.NONE);
            stageImageEditor.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void assignView() {
        FileChooser fileChooser = new FileChooser();
        assignFile = fileChooser.showOpenDialog(stage);
    }

    @FXML
    public void spekaView() {
        FileChooser fileChooser = new FileChooser();
        specFile = fileChooser.showOpenDialog(stage);
    }

    @FXML
    public void createVerify() {
        writeWorkbook(verifyAssign(assignFile, specFile, 0), "verifyAssign.xls", assignFile.getParent());
    }
}
