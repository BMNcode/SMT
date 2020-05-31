package org.bmn.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class CreateSBController {
    @FXML
    private StackPane zxc;
    @FXML
    private ScrollPane scroll;

    public void imageEditor() {
        try {
            Stage stageImageEditor = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/createSB.fxml"));
            Parent root = loader.load();
            stageImageEditor.setTitle("Image Editor");
            stageImageEditor.setScene(new Scene(root));
            stageImageEditor.initModality(Modality.NONE);
            stageImageEditor.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void LoadFile() {
        FileChooser fileChooser = new FileChooser();
        try {
            File fileLoadName = fileChooser.showOpenDialog(new Stage());
            FileInputStream fileInputStream = new FileInputStream(fileLoadName);
            Image image = new Image(fileInputStream);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            scroll.setContent(imageView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
