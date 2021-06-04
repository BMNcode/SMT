package org.bmn.controller;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static org.bmn.util.AssignConvert.assignConvert;
import static org.bmn.util.OutputResult.writeWorkbook;

public class MainController {


    @FXML
    public void onClickConvertPnP() {
        new ComponentController().convertPnPEditor();
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

    //кнопка перенаправляет в окно для конвертирования списка компонентов из программы в assign b .xls фаил для сборки
    @FXML
    public void onClickRedirectCreateAssign() {
        new CreateAssignController().createAssignEditor();
    }

}
