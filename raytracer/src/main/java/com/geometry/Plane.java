package com.geometry;

import com.imaging.Color;

public class Plane implements Shape {
    private final Point point;      // Un point du plan
    private final Vector normal;    // Vecteur normal au plan
    private final Color diffuse;
    private final Color specular;

    public Plane(Point point, Vector normal, Color diffuse, Color specular) {
        this.point = point;
        this.normal = normal.normalize(); // Normaliser le vecteur normal
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

    public Point getPoint() {
        return point;
    }

    public Vector getNormal() {
        return normal;
    }
}
