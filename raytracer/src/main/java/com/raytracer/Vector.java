package com.raytracer;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector v = (Vector) obj;
        return AbstractVec3.nearlyEqual(v.x, x)
            && AbstractVec3.nearlyEqual(v.y, y)
            && AbstractVec3.nearlyEqual(v.z, z);
    }

    // Getters et Setters
    public void setX(double x) {    
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    //-----Opérations vectorielles-----

    // Addition vectorielle  ex : v1.add(v2)
    public Vector add(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    // Soustraction vectorielle  ex : v1.subtract(v2)
    public Vector subtract(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    // Multiplication par un scalaire  ex : v1.scale(2) k*(1,2,3) = (k,2k,3k)
    public Vector scale(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    // Produit scalaire  ex : v1.dot(v2)
    public double dot(Vector v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    // Produit vectoriel ex : v1.cross(v2)
    public Vector cross(Vector v) {
        return new Vector(
            this.y * v.z - this.z * v.y,
            this.z * v.x - this.x * v.z,
            this.x * v.y - this.y * v.x
        );
    }

    // Longueur 
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    // Normalisation
    public Vector normalize() {
        double len = length();
        if (len == 0) {
            throw new ArithmeticException("Cannot normalize a zero-length vector");
        }
        return new Vector(this.x / len, this.y / len, this.z / len);
    }

}