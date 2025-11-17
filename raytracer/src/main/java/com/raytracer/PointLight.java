package com.raytracer;

import com.geometry.Point;
import com.geometry.Vector;
import com.imaging.Color;

public class PointLight implements Light {
    private final Point position;
    private final Color color;

    public PointLight(Point position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Point getPosition() { return position; }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public LightSample sampleAt(Point p) {
        // Direction vers la lumière = position - P
        Vector toLight = position.subtract(p);
        double dist = toLight.length();
        Vector L = dist > 0.0 ? toLight.scale(1.0 / dist) : new Vector(0, 0, 0);
        return new LightSample(L, dist, color);
    }
}
