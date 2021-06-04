package org.bmn.controller;

import com.gembox.spreadsheet.ExcelColumnCollection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bmn.service.ComponentService;
import org.bmn.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bmn.util.OutputResult.writeWorkbook;

public class CreateAssignController {

    public TableView table1;
    public TableView table2;
    public TextField deltaX;
    public TextField deltaY;

    String bufferTop = "";
    String bufferBot = "";


    public void createAssignEditor() {
        try {
            Stage stageImageEditor = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/createAssign.fxml"));
            Parent root = loader.load();
            stageImageEditor.setTitle("Create Assign");
            stageImageEditor.setScene(new Scene(root));
            stageImageEditor.initModality(Modality.NONE);
            stageImageEditor.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fillTable1() {

        //Copy and save buffer

        table1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            final KeyCodeCombination keyCodeCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);

            @Override
            public void handle(KeyEvent event) {
                if (keyCodeCombination.match(event)) {
                    Clipboard systemClipboard = Clipboard.getSystemClipboard();
                    String buffer = systemClipboard.getString();
                    bufferTop = buffer;

                    List<List<String>> clipboardDataTop = new ArrayList<>();


                    List<String> splitLineEnd = Arrays.asList(buffer.split("\\n"));

                    for (String s : splitLineEnd) {
                        clipboardDataTop.add(Arrays.asList(s.split("\\t")));
                    }

                    //create table
                    table1.getColumns().clear();

                    ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
                    for (List<String> row : clipboardDataTop)
                        data.add(FXCollections.observableArrayList(row));
                    table1.setItems(data);


                    for (int i = 0; i < clipboardDataTop.get(0).size(); i++) {
                        final int currentColumn = i;
                        TableColumn<ObservableList<String>, String> column = new TableColumn<>(ExcelColumnCollection.columnIndexToName(currentColumn));
                        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(currentColumn)));
                        column.setEditable(true);
                        column.setCellFactory(TextFieldTableCell.forTableColumn());
                        column.setOnEditCommit(
                                (TableColumn.CellEditEvent<ObservableList<String>, String> t) -> {
                                    t.getTableView().getItems().get(t.getTablePosition().getRow()).set(t.getTablePosition().getColumn(), t.getNewValue());
                                });
                        table1.getColumns().add(column);
                    }
                }
            }
        });
    }

    public void fillTable2() {

        //Copy and save buffer

        table2.setOnKeyPressed(new EventHandler<KeyEvent>() {
            final KeyCodeCombination keyCodeCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);

            @Override
            public void handle(KeyEvent event) {
                if (keyCodeCombination.match(event)) {

                    Clipboard systemClipboard = Clipboard.getSystemClipboard();
                    String buffer = systemClipboard.getString();
                    bufferBot = buffer;

                    List<List<String>> clipboardDataBot = new ArrayList<>();

                    ClipboardContent clipboardContent = new ClipboardContent();

                    List<String> splitLineEnd = Arrays.asList(buffer.split("\\n"));

                    for (String s : splitLineEnd) {
                        clipboardDataBot.add(Arrays.asList(s.split("\\t")));
                    }

                    //create table
                    table2.getColumns().clear();

                    ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
                    for (List<String> row : clipboardDataBot)
                        data.add(FXCollections.observableArrayList(row));
                    table2.setItems(data);


                    for (int i = 0; i < clipboardDataBot.get(0).size(); i++) {
                        final int currentColumn = i;
                        TableColumn<ObservableList<String>, String> column = new TableColumn<>(ExcelColumnCollection.columnIndexToName(currentColumn));
                        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(currentColumn)));
                        column.setEditable(true);
                        column.setCellFactory(TextFieldTableCell.forTableColumn());
                        column.setOnEditCommit(
                                (TableColumn.CellEditEvent<ObservableList<String>, String> t) -> {
                                    t.getTableView().getItems().get(t.getTablePosition().getRow()).set(t.getTablePosition().getColumn(), t.getNewValue());
                                });
                        table2.getColumns().add(column);
                    }
                }
            }
        });
    }


    //Save assign and tt or bb
    //tt and bb spec file to make SB
    public void onGenerateClick() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save assign and tt or bb");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xls", "*.xls"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (!bufferTop.isEmpty() || !bufferBot.isEmpty()) {
            String result = bufferTop + bufferBot;
            if (file != null) {
                writeWorkbook(Util.createWBWithoutBorder(new ComponentService().forCreateAssignInList(result)),
                        file.getName(), file.getParent());
            }
        }

        if (file != null) {

            String fileDirectory = file.getParent().concat("\\tempData");

            if (!bufferTop.isEmpty()) {
                if (Files.isDirectory(Paths.get(file.getParent() + "\\tempData"))) {
                    writeWorkbook(Util.createWBWithoutBorder(new ComponentService().forCreateTTOrBB(bufferTop)),
                            "tt.xls", fileDirectory);
                } else {
                    writeWorkbook(Util.createWB(new ComponentService().forCreateTTOrBB(bufferTop)),
                            "tt.xls", file.getParent());
                }
            }

            if (!bufferBot.isEmpty()) {
                if (Files.isDirectory(Paths.get(file.getParent() + "\\tempData"))) {
                    writeWorkbook(Util.createWBWithoutBorder(new ComponentService().forCreateTTOrBB(bufferBot)),
                            "bb.xls", fileDirectory);
                } else {
                    writeWorkbook(Util.createWB(new ComponentService().forCreateTTOrBB(bufferBot)),
                            "bb.xls", file.getParent());
                }
            }
        }

    }

    public void onFieldX() {
        deltaX.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[\\d]]")) {
                    deltaX.setText(newValue.replaceAll("[\\d]", "a"));
                }
            }
        });
    }

    public void onFieldY() {

        // force the field to be numeric only
        deltaY.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    deltaY.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
