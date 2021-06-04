package org.bmn.model;

import java.util.Objects;

public class Component {
    private String name;
    private String reference;
    private double locationX;
    private double locationY;
    private String side;
    private double rotation;
    private ComponentShape componentShape;
    private Boolean assign;

    public Component() {
    }

    public Component(double locationX, double locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public Component(String name, String reference) {
        this.name = name;
        this.reference = reference;
    }

    public Component(String name, String reference, Boolean assign) {
        this.name = name;
        this.reference = reference;
        this.assign = assign;
    }

    public Component(String name, String reference, Double locationX, Double locationY, Boolean assign) {
        this.name = name;
        this.reference = reference;
        this.locationX = locationX;
        this.locationY = locationY;
        this.assign = assign;
    }

    public Component(String reference, double locationX, double locationY, String side, double rotation) {
        this.reference = reference;
        this.locationX = locationX;
        this.locationY = locationY;
        this.side = side;
        this.rotation = rotation;
    }

    public Component(String name, String reference, double locationX, double locationY, double rotation) {
        this.name = name;
        this.reference = reference;
        this.locationX = locationX;
        this.locationY = locationY;
        this.rotation = rotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public ComponentShape getComponentShape() {
        return componentShape;
    }

    public void setComponentShape(ComponentShape componentShape) {
        this.componentShape = componentShape;
    }

    public Boolean getAssign() {
        return assign;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
    }

    public void flipPositive90(Component component) {
        //против часовой стрелки
        double temp = component.getLocationX();
        component.setLocationX(-component.getLocationY());
        component.setLocationY(temp);
        if (component.getRotation() != 0.0) {
            component.setRotation(component.getRotation() - 90.0);
        } else {
            component.setRotation(270.0);
        }
    }

    public void flipNegative90(Component component) {
        //по часовой стрелке
        double temp = component.getLocationX();
        component.setLocationX(component.getLocationY());
        component.setLocationY(-temp);
        if (component.getRotation() != 0.0) {
            component.setRotation(component.getRotation() - 90.0);
        } else {
            component.setRotation(270.0);
        }
        if (component.getRotation() < 270.0) {
            component.setRotation(component.getRotation() + 90.0);
        } else {
            component.setRotation(0.0 + (component.getRotation() - 270.0));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(reference, component.reference);
    }

    public boolean equalsName(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(name, component.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }

    @Override
    public String toString() {
        return "Component{" +
                "name='" + name + '\'' +
                ", reference='" + reference + '\'' +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                ", side='" + side + '\'' +
                ", rotation=" + rotation +
                '}';
    }

    public String customToString() {
        return reference + "    "
                + locationX + "     "
                + locationY + "     "
                + side + "     "
                + rotation;
    }
}
