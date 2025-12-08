package com.raytracer;

import com.imaging.Color;

/**
 * Interface commune pour toutes les sources lumineuses.
 * <p>
 * Implémentée par {@link DirectionalLight} et {@link PointLight}.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public interface Light {
    Color getColor();
}
