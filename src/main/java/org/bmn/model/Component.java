package org.bmn.model;

import java.util.Objects;

public class Component {
    private String name;
    private String reference;
    private double locationX;
    private double locationY;
    private String side;
    private double rotation;

    public Component() {
    }

    public Component(String name) {
        this.name = name;
    }

    public Component(double locationX, double locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public Component(Double aDouble) {
    }

    public Component(String reference, double locationX, double locationY, String side, double rotation) {
        this.reference = reference;
        this.locationX = locationX;
        this.locationY = locationY;
        this.side = side;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(name, component.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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
        return  reference + "    "
                + locationX + "     "
                + locationY + "     "
                + side + "     "
                + rotation;
    }
}
