package org.bmn.controller;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.bmn.util.AssignConvert.assignConvert;
import static org.bmn.util.OutputResult.outputCSV;
import static org.bmn.util.OutputResult.writeWorkbook;
import static org.bmn.util.PnPConvert.pnpList;

public class MainController {
    //кнопка выбора файла для преобразования PnP файла
    @FXML
    public void onClickConvertPnP() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Set<String> result = pnpList(file.toPath());
            outputCSV(result, "centroid", file.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickConvertAssign() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        writeWorkbook(assignConvert(file), "assignForPrint.xls", file.getParent());
    }

    //кнопка перенапрвляет в окно редактора сборки
    @FXML
    public void onClickRedirectSB() {
        new CreateSBController().imageEditor();
    }

    //кнопка перенаправляет в окно сверки assign и спеки
    @FXML
    public void onClickVerifyAssign() {
        new VerifyAssignController().verifyAssignEditor();
    }

}
