package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Figure {
    private final Point startPoint, endPoint;
    public Line(Point startPoint, Point endPoint, Color lineColor, Color fillColor, double lineSize){
        super(lineColor,fillColor,lineSize);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    @Override
    public boolean hasPoint(Point eventPoint){
        return false;
    }

    @Override
    public void move(double diffX, double diffY){
        startPoint.move(diffX, diffY);
        endPoint.move(diffX, diffY);
    }

    @Override
    public void draw(GraphicsContext gc, boolean selected){
        if(selected){
            gc.setStroke(Color.RED);
        }else {
            gc.setStroke(lineColor);
        }
        gc.setLineWidth(lineSize);
        gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
    }

    @Override
    public boolean isContained(Rectangle rectangle){
        return rectangle.hasPoint(startPoint) && rectangle.hasPoint(endPoint);
    }

    @Override
    public String toString() {
        return String.format("Linea [ %s , %s ]",startPoint,endPoint);
    }
}
