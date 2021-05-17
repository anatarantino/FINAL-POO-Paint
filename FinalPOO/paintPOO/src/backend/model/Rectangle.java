package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Figure {

    private Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight, Color lineColor, Color fillColor, double lineSize) {
        super(lineColor,fillColor,lineSize);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }


    @Override
    public void move(double diffX, double diffY){
        getTopLeft().move(diffX, diffY);
        getBottomRight().move(diffX, diffY);
    }

    @Override
    public void draw(GraphicsContext gc, boolean selected){
        gc.setFill(fillColor);
        if(selected){
            gc.setStroke(Color.RED);
        }else {
            gc.setStroke(lineColor);
    }
        gc.setLineWidth(lineSize);
        gc.fillRect(getTopLeft().getX(), getTopLeft().getY(),
                Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
        gc.strokeRect(getTopLeft().getX(), getTopLeft().getY(),
                Math.abs(getTopLeft().getX() - getBottomRight().getX()), Math.abs(getTopLeft().getY() - getBottomRight().getY()));
    }

    @Override
    public boolean hasPoint(Point eventPoint){
        return eventPoint.getX() > getTopLeft().getX() && eventPoint.getX() < getBottomRight().getX() &&
                eventPoint.getY() > getTopLeft().getY() && eventPoint.getY() < getBottomRight().getY();
    }

    @Override
    public boolean isContained(Rectangle rectangle){
        return rectangle.hasPoint(topLeft) && rectangle.hasPoint(bottomRight);
    }

}
