package backend.model;

import javafx.scene.paint.Color;

public class Square extends Rectangle {
    private final double side;
    public Square(Point topLeft, Point bottomRight, Color lineColor, Color fillColor, double lineSize) {
        super(topLeft,bottomRight,lineColor,fillColor,lineSize);
        side = topLeft.distanceXTo(bottomRight);
        bottomRight.moveTo(bottomRight.getX(),topLeft.getY()+side);
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s, %s , Lado: %s]", getTopLeft(), getBottomRight(), side);
    }
}
