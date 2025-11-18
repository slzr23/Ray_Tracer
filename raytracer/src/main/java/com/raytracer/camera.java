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

}