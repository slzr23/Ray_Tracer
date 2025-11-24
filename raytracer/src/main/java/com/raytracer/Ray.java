package com.raytracer;
import com.geometry.Point;
import com.geometry.Vector;

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
