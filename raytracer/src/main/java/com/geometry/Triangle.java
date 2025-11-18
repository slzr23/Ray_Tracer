package com.geometry;
import com.imaging.Color;

public class Triangle implements Shape {
    private final Point p1;
    private final Point p2;
    private final Point p3;
    private final Color diffuse;
    private final Color specular;
    
    public Triangle(Point p1, Point p2, Point p3, Color diffuse, Color specular) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
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