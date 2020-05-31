package org.bmn.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VerifyAssignController {

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
}
