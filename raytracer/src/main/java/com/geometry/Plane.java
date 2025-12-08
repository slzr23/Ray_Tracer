package com.geometry;

import java.util.Optional;
import com.raytracer.AABB;
import com.raytracer.Intersection;
import com.raytracer.Ray;
import com.imaging.Color;

/**
 * Plan infini défini par un point et une normale.
 * <p>
 * Utilisé typiquement pour le sol ou les murs. Ne possède pas
 * de bounding box (retourne null) et n'est donc pas accéléré par le BVH.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public class Plane implements Shape {
    private final Point point;      // Un point du plan
    private final Vector normal;    // Vecteur normal au plan
    private final Color diffuse;
    private final Color specular;
    private final Float shininess;

    public Plane(Point point, Vector normal, Color diffuse, Color specular, Float shininess) {
        this.point = point;
        this.normal = normal.normalize(); // Normaliser le vecteur normal
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    @Override
    public Color getDiffuse() {
        return diffuse;
    }

    @Override
    public Color getSpecular() {
        return specular;
    }

    @Override
    public Float getShininess() {
        return shininess;
    }

    public Point getPoint() {
        return point;
    }

    public Vector getNormal() {
        return normal;
    }

    @Override
    public Vector getNormalAt(Point p) {
        return normal;
    }

    @Override
    public Optional<Intersection> intersect(Ray ray) {
        double denom = normal.dot(ray.getDirection());
        if (Math.abs(denom) > 1e-6) {
            Vector p0l0 = new Vector(
                point.getX() - ray.getOrigin().getX(),
                point.getY() - ray.getOrigin().getY(),
                point.getZ() - ray.getOrigin().getZ()
            );
            double t = p0l0.dot(normal) / denom;
            if (t >= 0) {
                return Optional.of(new Intersection(this, t, ray.getPointAtParameter(t)));
            }
        }
        return Optional.empty();
    }

    @Override
    public AABB getBoundingBox() {
        return null; // Infinite plane -> handled outside BVH
    }
}
