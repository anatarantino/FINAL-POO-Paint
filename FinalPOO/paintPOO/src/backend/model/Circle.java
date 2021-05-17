package backend.model;

import javafx.scene.paint.Color;

public class Circle extends Ellipse {

    private final double radius;
    public Circle(Point topLeft, Point bottomRight, Color lineColor, Color fillColor, double lineSize) {
        super(topLeft, bottomRight, lineColor, fillColor, lineSize);
        this.radius = getXAxis();
        setYAxis(getXAxis());
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", getCenterPoint(), radius);
    }

}
