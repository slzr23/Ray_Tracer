package com.geometry;

import com.raytracer.AbstractVec3;

public class Point {
    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    // Equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point p = (Point) obj;
        return AbstractVec3.nearlyEqual(p.x, x)
            && AbstractVec3.nearlyEqual(p.y, y)
            && AbstractVec3.nearlyEqual(p.z, z);
    }


    // Getters et Setters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    // -----Opérations sur les points----- 

    // Soustraction 
    public Vector subtract(Point p) {
        return new Vector(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    // Multiplication par un scalaire
    public Point scale(double scalar) {
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }

}
