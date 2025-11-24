package com.raytracer;
import com.geometry.Point;
import com.geometry.Shape;

public class Intersection {
    private final Shape shape;    // objet touché
    private final double t;        // dist depuis l'origine du rayon
    private final Point point;     // point d'intersection
    
    public Intersection(Shape shape, double t, Point point) {
        this.shape = shape;
        this.t = t;
        this.point = point;
    }
    
    public Shape getShape() {
        return shape;
    }
    
    public double getT() {
        return t;
    }
    
    public Point getPoint() {
        return point;
    }
}