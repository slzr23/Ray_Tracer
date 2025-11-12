package com.raytracer;

/**
 * Base util class for 3D operations and numeric helpers.
 * Components are doubles. This class provides a shared epsilon-based
 * comparison and simple helpers usable by Point and Vector.
 */
public class AbstractVec3 {
    // Epsilon utilisé pour les comparaisons de nombres flottants
    public static final double EPS = 1e-9;

    // Retourne vrai si les deux doubles sont à peu près égaux dans EPS.
    public static boolean nearlyEqual(double a, double b) {
        return Math.abs(a - b) <= EPS;
    }

    // Clamp une valeur entre min et max
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}