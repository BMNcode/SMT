package org.bmn.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bmn.model.Component;
import org.bmn.service.ComponentService;
import org.bmn.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bmn.util.OutputResult.outputCSV;

public class ComponentController {


    public RadioButton turnLeft;
    public RadioButton turnRight;

    private String pattern = "[xX]{1}[-]?(\\d)*[yY]{1}[-]?(\\d)*";

    public void convertPnPEditor() {
        try {
            Stage stageImageEditor = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/convertPnP.fxml"));
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
    public void convertAltiumPnP() {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            List<Component> componentsAD = new ComponentService().findAllinAD(file.toPath());

            if (turnLeft.isSelected()) {
                componentsAD.forEach(s -> {
                    s.flipPositive90(s);
                });
            }

            if (turnRight.isSelected()) {
                componentsAD.forEach(s -> {
                    s.flipNegative90(s);
                });
            }
            Set<String> resultAD = componentsAD.stream()
                    .map(Component::customToString)
                    .collect(Collectors.toSet());
            outputCSV(Util.spaceAlign(resultAD, "\\s+", 20), "centroid", file.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void convertGerberPnP() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            List<Component> componentsDrl = new ComponentService().findAllinGerber(file.toPath(), pattern);

            if (turnLeft.isSelected()) {
                componentsDrl.forEach(s -> {
                    s.flipPositive90(s);
                });
            }

            if (turnRight.isSelected()) {
                componentsDrl.forEach(s -> {
                    s.flipNegative90(s);
                });
            }
            Set<String> resultDrl = componentsDrl.stream()
                    .map(s -> Util.formatDouble(s.getLocationX()) + "       " + Util.formatDouble(s.getLocationY()))
                    .collect(Collectors.toSet());
            outputCSV(Util.spaceAlign(resultDrl, "\\s+", 20), "centroid", file.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void convertPCADPnP() {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            List<Component> componentsPnp = new ComponentService().findAllinPCAD(file.toPath());

            if (turnLeft.isSelected()) {
                componentsPnp.forEach(s -> {
                    s.flipPositive90(s);
                });
            }

            if (turnRight.isSelected()) {
                componentsPnp.forEach(s -> {
                    s.flipNegative90(s);
                });
            }

            Set<String> resultPnp = componentsPnp.stream()
                    .map(Component::customToString)
                    .collect(Collectors.toSet());
            outputCSV(Util.spaceAlign(resultPnp, "\\s+", 20), "centroid", file.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
