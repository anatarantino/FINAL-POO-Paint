package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ellipse extends Figure {
    private final Point centerPoint;
    private double xAxis, yAxis;
    private Point topLeft;
    private Point bottomRight;

    public Ellipse(Point topLeft, Point bottomRight, Color lineColor, Color fillColor, double lineSize) {
        super(lineColor, fillColor, lineSize);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        yAxis = (topLeft.distanceYTo(bottomRight));
        xAxis = (topLeft.distanceXTo(bottomRight));
        centerPoint = new Point(topLeft.getX() + xAxis/2, topLeft.getY() + yAxis/2);
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, Eje horizontal: %.2f, Eje vertical: %.2f]", centerPoint, xAxis, yAxis);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getXAxis() {
        return xAxis;
    }

    public double getYAxis(){
        return yAxis;
    }

    public void setYAxis(double yAxis){
        this.yAxis = yAxis;
    }

    @Override
    public void move(double diffX, double diffY){
        centerPoint.move(diffX, diffY);
        topLeft.move(diffX,diffY);
        bottomRight.move(diffX,diffY);
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
        gc.fillOval(getCenterPoint().getX() - getXAxis() / 2, getCenterPoint().getY() - getYAxis() / 2, xAxis, yAxis);
        gc.strokeOval(getCenterPoint().getX() - getXAxis() / 2, getCenterPoint().getY() - getYAxis() / 2, xAxis, yAxis);
    }

    @Override
    public boolean hasPoint(Point eventPoint){
        return ((Math.pow(eventPoint.getX() - centerPoint.getX(), 2) / Math.pow(xAxis/2, 2)) + (Math.pow(eventPoint.getY() - centerPoint.getY(), 2) / Math.pow(yAxis/2, 2))) <= 1;
    }

    @Override
    public boolean isContained(Rectangle rectangle){
        return rectangle.hasPoint(topLeft) && rectangle.hasPoint(bottomRight);
    }
}
