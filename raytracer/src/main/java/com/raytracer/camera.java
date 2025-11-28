package com.raytracer;

import com.geometry.*;

public class Camera {
    private Point position;
    private Point lookAt;
    private Vector up;
    private double fov;

    public Camera(Point position, Point lookAt, Vector up, double fov) {
        this.position = position;
        this.lookAt = lookAt;
        this.up = up;
        this.fov = fov;
    }

    public Point getPosition() {
        return position;
    }

    public Point getLookAt() {
        return lookAt;
    }

    public Vector getUp() {
        return up;
    }

    public double getFov() {
        return fov;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Camera other = (Camera) obj;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (lookAt == null) {
            if (other.lookAt != null)
                return false;
        } else if (!lookAt.equals(other.lookAt))
            return false;
        if (up == null) {
            if (other.up != null)
                return false;
        } else if (!up.equals(other.up))
            return false;
        if (Double.doubleToLongBits(fov) != Double.doubleToLongBits(other.fov))
            return false;
        return true;
    }

    /**repère orthonormé de la caméra**/
    public Orthonormal getOrthonormal() {
        // w = (lookFrom - lookAt) normalisé
        Vector w = new Vector(
            position.getX() - lookAt.getX(),
            position.getY() - lookAt.getY(),
            position.getZ() - lookAt.getZ()
        ).normalize();
        
        // u = (up × w) normalisé
        Vector u = up.cross(w).normalize();
        
        // v = (w × u) normalisé
        Vector v = w.cross(u).normalize();
        
        return new Orthonormal(u, v, w);
    }

    /** hauteur d'un pixel dans la scène**/
    public double getPixelHeight() {
        double fovr = Math.toRadians(fov);
        return Math.tan(fovr / 2.0);
    }

    /** largeur d'un pixel dans la scène**/
    public double getPixelWidth(int imgWidth, int imgHeight) {
        double pixelHeight = getPixelHeight();
        return pixelHeight * ((double) imgWidth / imgHeight);
    }

}