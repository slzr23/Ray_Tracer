package com.raytracer;

import com.geometry.Vector;
import com.imaging.Color;

public class DirectionalLight implements Light {
    private final Vector direction;
    private final Color color;

    public DirectionalLight(Vector direction, Color color) {
        this.direction = direction;
        this.color = color;
    }

    public Vector getDirection() {
        return direction;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
