package org.bmn.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bmn.model.Component;
import org.bmn.service.ComponentService;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class CreateSBController {
    public StackPane stackPane;
    public TextField panelX;
    public TextField panelY;
    @FXML
    private ScrollPane scroll;
    Image image;
    private double zeroPointX;
    private double zeroPointY;




    private String pattern = "[xX]{1}[-]?(\\d)*[yY]{1}[-]?(\\d)*";

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
//        FileChooser fileChooser = new FileChooser();
        try {
//            File fileLoadName = fileChooser.showOpenDialog(new Stage());
//            FileInputStream fileInputStream = new FileInputStream(fileLoadName);
//            image = new Image(fileInputStream);
//            ImageView imageView = new ImageView(image);
//            stackPane.getChildren().add(imageView);
            FileInputStream fileInputStream = new FileInputStream("E:\\work\\test_razmetka_5000.png");
            image = new Image(fileInputStream);
            ImageView imageView = new ImageView(image);
            stackPane.getChildren().add(imageView);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void loadComponents() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            List<Component> listComponents = new ComponentService().findAllinGerber(file.toPath(), pattern);

            for (Component ch : listComponents) {
                double x = ch.getLocationX()/1000;
                double y = ch.getLocationY()/1000;
                Rectangle rectangle = new Rectangle(20, 10);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(2.0);
                rectangle.setFill(Color.WHITE);
                //rectangle.setRotate(45.0);
                ch.setShape(rectangle);
                stackPane.getChildren().add(rectangle);
                StackPane.setAlignment(rectangle, Pos.BOTTOM_LEFT);
                StackPane.setMargin(rectangle, new Insets(0, 0, y + zeroPointY, x + zeroPointX));

                scroll.setContent(stackPane);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPanel() {
        //чтение вводимых размеров
        double panelSizeX = Double.parseDouble(panelX.getText());
        double panelSizeY = Double.parseDouble(panelY.getText());

        //определение середины холста
        double imageMidX = image.getWidth()/2.0;
        double imageMidY = image.getHeight()/2.0;


        //определение размеров исходного холста
        double imageX = image.getWidth();
        double imageY = image.getHeight();

        //маштабирование границ панели
        //коэффициент маштабирования
        double indexScalePanel = panelSizeX >= panelSizeY ? panelSizeY/panelSizeX : panelSizeX/panelSizeY;
        double panelSizeScaleX = panelSizeX >= panelSizeY ? imageX : indexScalePanel * imageX;
        double panelSizeScaleY = panelSizeY >= panelSizeX ? imageY : indexScalePanel * imageY;

        //нахождение точки отсчета панели
        zeroPointX = imageMidX - panelSizeScaleX/2;
        zeroPointY = imageMidY - panelSizeScaleY/2;


        //создание границ прямоугольной панели
        Rectangle panelOutline = new Rectangle(panelSizeScaleX, panelSizeScaleY);
        panelOutline.setStroke(Color.BLACK);
        panelOutline.setStrokeWidth(4);
        panelOutline.setFill(Color.WHITE);

        //добавление панели на стакпэйн
        stackPane.getChildren().add(panelOutline);
        StackPane.setAlignment(panelOutline, Pos.BOTTOM_LEFT);
        StackPane.setMargin(panelOutline, new Insets(0, 0, imageMidY - panelSizeScaleY/2, imageMidX - panelSizeScaleX/2));
        scroll.setContent(stackPane);
    }
}
