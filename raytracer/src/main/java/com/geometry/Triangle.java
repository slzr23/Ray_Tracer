package com.geometry;

import java.util.Optional;
import com.raytracer.AABB;
import com.raytracer.Intersection;
import com.raytracer.Ray;
import com.imaging.Color;

/**
 * Triangle défini par trois sommets.
 * <p>
 * L'intersection rayon-triangle utilise l'algorithme de Möller-Trumbore.
 * La normale est calculée par produit vectoriel des arêtes.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public class Triangle implements Shape {
    private final Point p1;
    private final Point p2;
    private final Point p3;
    private final Color diffuse;
    private final Color specular;
    protected final Float shininess;
    
    public Triangle(Point p1, Point p2, Point p3, Color diffuse, Color specular, Float shininess) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
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

    // Getters pour les points
    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public Point getP3() {
        return p3;
    }

    @Override
    public Vector getNormalAt(Point p) {
        Vector v1 = new Vector(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
        Vector v2 = new Vector(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
        return v1.cross(v2).normalize();
    }

    @Override
    public Optional<Intersection> intersect(Ray ray) {
        final double EPSILON = 1e-8;
        Vector edge1 = new Vector(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
        Vector edge2 = new Vector(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
        Vector h = ray.getDirection().cross(edge2);
        double a = edge1.dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return Optional.empty(); // Rayon parallèle au triangle
        }

        double f = 1.0 / a;
        Vector s = new Vector(
            ray.getOrigin().getX() - p1.getX(),
            ray.getOrigin().getY() - p1.getY(),
            ray.getOrigin().getZ() - p1.getZ()
        );
        double u = f * s.dot(h);

        if (u < -EPSILON || u > 1.0 + EPSILON) {
            return Optional.empty();
        }

        Vector q = s.cross(edge1);
        double v = f * ray.getDirection().dot(q);

        if (v < -EPSILON || u + v > 1.0 + EPSILON) {
            return Optional.empty();
        }

        double t = f * edge2.dot(q);

        if (t > EPSILON) {
            return Optional.of(new Intersection(this, t, ray.getPointAtParameter(t)));
        }

        return Optional.empty();
    }

    @Override
    public AABB getBoundingBox() {
        double minX = Math.min(p1.getX(), Math.min(p2.getX(), p3.getX()));
        double minY = Math.min(p1.getY(), Math.min(p2.getY(), p3.getY()));
        double minZ = Math.min(p1.getZ(), Math.min(p2.getZ(), p3.getZ()));
        double maxX = Math.max(p1.getX(), Math.max(p2.getX(), p3.getX()));
        double maxY = Math.max(p1.getY(), Math.max(p2.getY(), p3.getY()));
        double maxZ = Math.max(p1.getZ(), Math.max(p2.getZ(), p3.getZ()));
        return new AABB(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Triangle other = (Triangle) obj;
        if (p1 == null) {
            if (other.p1 != null)
                return false;
        } else if (!p1.equals(other.p1))
            return false;
        if (p2 == null) {
            if (other.p2 != null)
                return false;
        } else if (!p2.equals(other.p2))
            return false;
        if (p3 == null) {
            if (other.p3 != null)
                return false;
        } else if (!p3.equals(other.p3))
            return false;
        if (diffuse == null) {
            if (other.diffuse != null)
                return false;
        } else if (!diffuse.equals(other.diffuse))
            return false;
        if (specular == null) {
            if (other.specular != null)
                return false;
        } else if (!specular.equals(other.specular))
            return false;
        return true;
    }
}