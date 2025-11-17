package com.raytracer;

import com.geometry.Point;
import com.geometry.Vector;
import com.imaging.Color;

public class DirectionalLight extends AbstractLight {
    // Direction de propagation de la lumière (du sol vers la scene) normalisée
    private final Vector direction;

    public DirectionalLight(Vector direction, Color color) {
        super(color);// Passe la couleur au parent AbstractLight
        this.direction = direction.normalize();
    }

    @Override
    public LightSample sampleAt(Point p) {
        // Depuis P la lumière arrive de l'opposé de sa direction de propagation
        Vector L = direction.scale(-1).normalize();
        return new LightSample(L, Double.POSITIVE_INFINITY, color);
    }
}
