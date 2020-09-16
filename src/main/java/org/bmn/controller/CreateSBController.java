package org.bmn.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bmn.model.Component;
import org.bmn.model.ComponentShape;
import org.bmn.service.ComponentService;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class CreateSBController {
    public StackPane stackPane;
    public TextField panelX;
    public TextField panelY;
    public Button save;
    @FXML
    private ScrollPane scroll;
    Image image;
    private double zeroPointX;
    private double zeroPointY;
    private double indexScalePanel;
    private double panelSizeX;
    private double panelSizeY;
    private double panelSizeScaleX;
    private double panelSizeScaleY;
    private double imageX;
    private double imageY;



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
            FileInputStream fileInputStream = new FileInputStream("D:\\Java\\JavaProject\\SMT\\src\\main\\resources\\templateCanvas\\A3_550px_vert.png");
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
            List<Component> listComponents = new ComponentService().findAllinXLS(file.toPath(), 1, 2, 6,
                    3, 4, 5, "," );

            for (Component ch : listComponents) {
                double x = ch.getLocationX();
                double y = ch.getLocationY();
                Rectangle rectangle = new Rectangle(50, 25);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(4);
                rectangle.setFill(Color.WHITE);
                rectangle.setRotate(ch.getRotation());
                ch.setComponentShape(new ComponentShape("Rectangle", rectangle));
                stackPane.getChildren().add(rectangle);
                StackPane.setAlignment(rectangle, Pos.BOTTOM_LEFT);
                StackPane.setMargin(rectangle, new Insets(0, 0, zeroPointY + (y / panelSizeY) * panelSizeScaleY,
                        zeroPointX + (x / panelSizeX) * panelSizeScaleX));

                scroll.setContent(stackPane);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPanel() {
        //чтение вводимых размеров
        panelSizeX = Double.parseDouble(panelX.getText());
        panelSizeY = Double.parseDouble(panelY.getText());

        //определение середины холста
        double imageMidX = image.getWidth()/2.0;
        double imageMidY = image.getHeight()/2.0;


        //определение размеров исходного холста
        imageX = image.getWidth();
        imageY = image.getHeight();

        //маштабирование границ панели
        //коэффициент маштабирования
        indexScalePanel = panelSizeX >= panelSizeY ? panelSizeY/panelSizeX : panelSizeX/panelSizeY;
        panelSizeScaleX = panelSizeX >= panelSizeY ? imageX : indexScalePanel * imageX;
        panelSizeScaleY = panelSizeY >= panelSizeX ? imageY : indexScalePanel * imageY;

        //нахождение точки отсчета панели
        zeroPointX = imageMidX - panelSizeScaleX/2;
        zeroPointY = imageMidY - panelSizeScaleY/2;


        //создание границ прямоугольной панели
        Rectangle panelOutline = new Rectangle(panelSizeScaleX, panelSizeScaleY);
        panelOutline.setStroke(Color.BLACK);
        panelOutline.setStrokeWidth(12);
        panelOutline.setFill(Color.WHITE);

        //добавление панели на стакпэйн
        stackPane.getChildren().add(panelOutline);
        StackPane.setAlignment(panelOutline, Pos.BOTTOM_LEFT);
        StackPane.setMargin(panelOutline, new Insets(0, 0, imageMidY - panelSizeScaleY/2, imageMidX - panelSizeScaleX/2));
        scroll.setContent(stackPane);
    }

    public void captureAndSaveDisplay(){
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

        //Prompt user to select a file
        File file = fileChooser.showSaveDialog(null);

        if(file != null){
            try {
                //Pad the capture area
                WritableImage writableImage = new WritableImage((int)imageX + 5, (int)imageY + 5);
                stackPane.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                //Write the snapshot to the chosen file
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }


}
