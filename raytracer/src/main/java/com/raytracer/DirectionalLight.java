package com.raytracer;

import com.geometry.Point;
import com.geometry.Vector;
import com.imaging.Color;

public class DirectionalLight implements Light {
    // Direction de propagation de la lumière (du sol vers la scene) normalisée
    private final Vector direction;
    private final Color color;

    public DirectionalLight(Vector direction, Color color) {
        this.direction = direction.normalize();
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public LightSample sampleAt(Point p) {
        // Depuis P la lumière arrive de l'opposé de sa direction de propagation
        Vector L = direction.scale(-1).normalize();
        return new LightSample(L, Double.POSITIVE_INFINITY, color);
    }
}
