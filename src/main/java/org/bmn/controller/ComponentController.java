package org.bmn.controller;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
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
    String pattern = "[xX]{1}[-]?(\\d)*[yY]{1}[-]?(\\d)*";

    @FXML
    public void onClickConvertPnP() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            switch (Util.getFileExtension(file)) {
                case ("drl"):
                    List<Component> componentsDrl = new ComponentService().findAllinGerber(file.toPath(), pattern);
                    Set<String> resultDrl = componentsDrl.stream()
                            .map(s -> Util.formatDouble(s.getLocationX()) + "       " + Util.formatDouble(s.getLocationY()))
                            .collect(Collectors.toSet());
                    outputCSV(Util.spaceAlign(resultDrl, "\\s+", 20), "centroid", file.getParent());
                    break;
                case ("pnp"):
                    List<Component> componentsPnp = new ComponentService().findAllinPCAD(file.toPath());
                    Set<String> resultPnp = componentsPnp.stream()
                            .map(Component::customToString)
                            .collect(Collectors.toSet());
                    outputCSV(Util.spaceAlign(resultPnp, "\\s+", 20), "centroid", file.getParent());
                    break;
                case ("txt"):
                    List<Component> componentsAD = new ComponentService().findAllinAD(file.toPath());
                    Set<String> resultAD = componentsAD.stream()
                            .map(Component::customToString)
                            .collect(Collectors.toSet());
                    outputCSV(Util.spaceAlign(resultAD, "\\s+", 20), "centroid", file.getParent());
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
