package com.geometry;

import java.util.Optional;

import com.imaging.Color;
import com.raytracer.AABB;
import com.raytracer.Intersection;
import com.raytracer.Ray;

public class Sphere implements Shape {
    private final Point center;
    private final double radius;
    private final Color diffuse;
    private final Color specular;
    private final Float shininess;

    public Sphere(Point center, double radius, Color diffuse, Color specular, Float shininess) {
        this.center = center;
        this.radius = radius;
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

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    /**
     * Calcule la normale à la surface de la sphère au point p
     * Formule : n = (p - Center) / ||p - Center||
     * @param p le point à la surface de la sphère
     * @return le vecteur normal normalisé
     */
    @Override
    public Vector getNormalAt(Point p) {
        // Vecteur du centre vers le point
        Vector normal = new Vector(
            p.getX() - center.getX(),
            p.getY() - center.getY(),
            p.getZ() - center.getZ()
        );
        
        // Normaliser le vecteur
        return normal.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sphere other = (Sphere) obj;
        if (center == null) {
            if (other.center != null)
                return false;
        } else if (!center.equals(other.center))
            return false;
        if (Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius))
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

    /**Calcule intersection rayon / sphère
    * @param ray le rayon à tester
    * @return Optional contenant l'Intersection si elle existe, sinon vide
    */
    @Override
    public Optional<Intersection> intersect(Ray ray) {
        Point o = ray.getOrigin();
        Vector d = ray.getDirection();
        
        // Vecteur (o - c)
        Vector oc = new Vector(
            o.getX() - center.getX(),
            o.getY() - center.getY(),
            o.getZ() - center.getZ()
        );
        
        // Coeff eq second degré
        double a = d.dot(d);
        double b = 2.0 * oc.dot(d);
        double c = oc.dot(oc) - radius * radius;
        
        // Discriminant
        double delta = b * b - 4 * a * c;
        
        // Pas d'intersection
        if (delta < 0) {
            return Optional.empty();
        }
        
        // Calculer t2 car plus proche
        double t2 = (-b - Math.sqrt(delta)) / (2 * a);
        
        // Si t2 est négatif, essayer t1
        if (t2 < 0) {
            double t1 = (-b + Math.sqrt(delta)) / (2 * a);
            if (t1 < 0) {
                return Optional.empty(); // Les deux sont derrière la caméra
            }
            t2 = t1;
        }
        
        // point d'intersection
        Point intersectionPoint = ray.getPointAtParameter(t2);
        
        return Optional.of(new Intersection(this, t2, intersectionPoint));
    }

    @Override
    public AABB getBoundingBox() {
        double r = radius;
        Point min = new Point(center.getX() - r, center.getY() - r, center.getZ() - r);
        Point max = new Point(center.getX() + r, center.getY() + r, center.getZ() + r);
        return new AABB(min, max);
    }

}
