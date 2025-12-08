package com.raytracer;
import com.geometry.Point;
import com.geometry.Vector;

/**
 * Représente un rayon dans l'espace 3D.
 * <p>
 * Un rayon est défini par une origine (point de départ) et une direction.
 * Équation paramétrique : P(t) = origine + t * direction, avec t >= 0.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public class Ray {
    private final Point origin;
    private final Vector direction;

    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize(); // Normaliser la direction
    }

    public Point getOrigin() {
        return origin;
    }

    public Vector getDirection() {
        return direction;
    }    

    /**
     * Calcule un point sur le rayon à la distance t
     * p = origin + t * direction
     */
    public Point getPointAtParameter(double t) {
        return new Point (
            origin.getX() + t * direction.getX(),
            origin.getY() + t * direction.getY(),
            origin.getZ() + t * direction.getZ()
        );
    }
}
