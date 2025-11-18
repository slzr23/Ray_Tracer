package com.geometry;

import com.imaging.Color;

public class Sphere implements Shape {
    private final Point center;
    private final double radius;
    private final Color diffuse;
    private final Color specular;

    public Sphere(Point center, double radius, Color diffuse, Color specular) {
        this.center = center;
        this.radius = radius;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    @Override
    public Color getDiffuse() {
        return diffuse;
    }

    @Override
    public Color getSpecular() {
        return specular;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
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

}
