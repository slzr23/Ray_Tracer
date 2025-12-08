package com.raytracer;

import com.geometry.Vector;
import com.imaging.Color;

/**
 * Source lumineuse directionnelle (type soleil).
 * <p>
 * Émet des rayons parallèles dans une direction donnée, sans atténuation
 * liée à la distance. Utilisée pour simuler des sources très éloignées.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
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
