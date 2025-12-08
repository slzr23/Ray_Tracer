package com.raytracer;

import com.geometry.Point;
import com.imaging.Color;

/**
 * Source lumineuse ponctuelle.
 * <p>
 * Émet de la lumière dans toutes les directions depuis un point précis
 * dans l'espace. La distance à la source est prise en compte pour
 * le calcul des ombres.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
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
