package com.raytracer;

import com.geometry.Vector;
import com.imaging.Color;

public class LightSample {
    // Direction normalisée depuis le point P vers la lumière
    private final Vector direction;
    // Distance jusqu'à la lumière (Double.POSITIVE_INFINITY pour une directionnelle)
    private final double maxDistance;
    // Couleur/intensité de la lumière
    private final Color color;

    public LightSample(Vector direction, double maxDistance, Color color) {
        this.direction = direction;
        this.maxDistance = maxDistance;
        this.color = color;
    }

    public Vector getDirection() { return direction; }
    public double getMaxDistance() { return maxDistance; }
    public Color getColor() { return color; }
}
