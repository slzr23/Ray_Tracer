package com.raytracer;

import com.geometry.Point;
import com.imaging.Color;

public class PointLight implements Light {
    private final Point position;
    private final Color color;

    public PointLight(Point position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
