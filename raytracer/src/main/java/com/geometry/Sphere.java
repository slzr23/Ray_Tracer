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

}
