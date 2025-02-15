package backend.model;

public class Point implements Movable {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }

    @Override
    public void move(double diffX, double diffY){
        x += diffX;
        y += diffY;
    }

    public void moveTo(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double distanceXTo(Point point){
        return Math.abs(point.getX() - x);
    }
    public double distanceYTo(Point point){
        return Math.abs(point.getY() - y);
    }

}
