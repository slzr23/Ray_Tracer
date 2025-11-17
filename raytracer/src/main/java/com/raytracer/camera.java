package com.raytracer;

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

}