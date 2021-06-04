package org.bmn.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bmn.model.Component;
import org.bmn.service.ComponentService;
import org.bmn.util.Util;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.bmn.util.OutputResult.writeWorkbook;

public class VerifyAssignController {

    public TextField specPath;
    public TextField assignPath;
    public TextField refSpecColumn;
    public TextField partSpecColumn;
    public TextField assignRefColumn;
    public TextField assignPartColumn;
    public TextField specSheet;
    public TextField assignSheet;
    public ScrollPane textScroll;
    public TextArea textAssign;
    public AnchorPane textPane;


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
    public void spekaView() {
        FileChooser fileChooser = new FileChooser();
        specFile = fileChooser.showOpenDialog(stage);
        specPath.appendText(specFile.getAbsolutePath());
    }

    @FXML
    public void assignView() {
        FileChooser fileChooser = new FileChooser();
        assignFile = fileChooser.showOpenDialog(stage);
        assignPath.appendText(assignFile.getAbsolutePath());
    }

    //сформировать фаил xls из двух исходных
    @FXML
    public void createVerify() {
        try {
            List<Component> specComponentsList = new ComponentService().findAllinXLS(specFile.toPath(),
                    Integer.parseInt(specSheet.getText()), Integer.parseInt(refSpecColumn.getText()),
                    Integer.parseInt(partSpecColumn.getText()), ",");

            List<Component> assignComponentsList = new ComponentService().findAllinXLS(assignFile.toPath(),
                    Integer.parseInt(assignSheet.getText()), Integer.parseInt(assignRefColumn.getText()),
                    Integer.parseInt(assignPartColumn.getText()), ",");


            writeWorkbook(Util.createWB(Util.unionPartDataForVerifyComponents(assignComponentsList, specComponentsList)),
                    "verifyAssign.xls", assignFile.getParent());


        } catch (Exception e) {
            e.printStackTrace();
        }
//        writeWorkbook(verifyAssign(assignFile, specFile, 0), "verifyAssign.xls", assignFile.getParent());
    }

    public void onScreen() {
        try {
            Stage stageImageEditor = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/verifyAssignOnScreen.fxml"));
            Parent root = loader.load();
            stageImageEditor.setTitle("Verify Assign");
            stageImageEditor.setScene(new Scene(root));
            stageImageEditor.initModality(Modality.NONE);
            stageImageEditor.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textAssign.setText("aaaaaaaaaaaaa");
        textScroll.setContent(textAssign);

//        try {
//            List<Component> specComponentsList = new ComponentService().findAllinXLS(specFile.toPath(),
//                    Integer.parseInt(specSheet.getText()), Integer.parseInt(refSpecColumn.getText()),
//                    Integer.parseInt(partSpecColumn.getText()), ",");
//
//            List<Component> assignComponentsList = new ComponentService().findAllinXLS(assignFile.toPath(),
//                    Integer.parseInt(assignSheet.getText()), Integer.parseInt(assignRefColumn.getText()),
//                    Integer.parseInt(assignPartColumn.getText()), ",");
//
//            for(String s : Util.unionPartDataForVerifyComponents(assignComponentsList, specComponentsList)) {
//
//                textAssign.setText(s);
//                textAssign.setText("//n");
//            }
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }




}
