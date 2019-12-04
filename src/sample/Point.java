package sample;

import javafx.geometry.Point2D;

public class Point {
    private Point2D center;
    private int id;

    Point(Point2D center, int id){
        this.center = center;
        this.id = id;
    }

    Point(){
    }

    public int getId(){
        return id;
    }

    public Point2D getCenter() {
        return center;
    }
}
