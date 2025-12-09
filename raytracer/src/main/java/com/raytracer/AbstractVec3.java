package com.raytracer;

/**
 * Classe utilitaire de base pour les opérations en trois dimensions.
 * <p>
 * Fournit des méthodes statiques communes utilisées par les classes
 * géométriques du projet, notamment pour les comparaisons numériques
 * avec tolérance (epsilon) et les opérations de bornage.
 * </p>
 * <p>
 * Les comparaisons à virgule flottante en informatique graphique
 * nécessitent une marge d'erreur pour compenser les imprécisions
 * inhérentes à la représentation binaire des nombres réels.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public class AbstractVec3 {
    // Epsilon utilisé pour les comparaisons de nombres flottants
    public static final double EPS = 1e-9;

    // Retourne vrai si les deux doubles sont à peu près égaux à epslion près
    public static boolean nearlyEqual(double a, double b) {
        return Math.abs(a - b) <= EPS;
    }

    // Clamp une valeur entre min et max
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}