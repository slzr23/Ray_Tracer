package com.raytracer;
import com.geometry.Point;
import com.geometry.Shape;

/**
 * Résultat d'une intersection rayon-objet.
 * <p>
 * Contient l'objet touché, le paramètre t (distance le long du rayon)
 * et le point d'intersection dans l'espace 3D.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
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