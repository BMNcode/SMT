package org.bmn.model;

import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentShape {

    private String shapeName;
    private Shape shape;

    public ComponentShape(String shapeName, Shape shape) {
        this.shapeName = shapeName;
        this.shape = shape;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

}
