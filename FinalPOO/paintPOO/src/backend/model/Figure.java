package backend.model;

import javafx.scene.paint.Color;

public abstract class Figure implements Movable, Drawable{
    protected Color lineColor;
    protected Color fillColor;
    protected double lineSize;
    public Figure(Color lineColor, Color fillColor, double lineSize){
        this.lineColor = lineColor;
        this.fillColor = fillColor;
        this.lineSize = lineSize;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public double getLineSize() {
        return lineSize;
    }

    public void setLineSize(double lineSize) {
        this.lineSize = lineSize;
    }

    public abstract boolean hasPoint(Point eventPoint);

    public abstract boolean isContained(Rectangle rectangle);
}
