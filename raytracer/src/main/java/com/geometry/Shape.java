package com.geometry;

import java.util.Optional;
import com.raytracer.AABB;
import com.raytracer.Intersection;
import com.raytracer.Ray;
import com.imaging.Color;

/**
 * Interface pour les objets géométriques de la scène.
 * <p>
 * Tout objet pouvant être rendu doit implémenter cette interface :
 * sphères, triangles, plans. Définit les propriétés matériau
 * (diffuse, speculaire, shininess) et les méthodes d'intersection.
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public interface Shape {
    Color getDiffuse();
    Color getSpecular();
    Float getShininess();
    Vector getNormalAt(Point p);
    Optional<Intersection> intersect(Ray ray);
    /**
     * Bounding box for acceleration. Return null for unbounded shapes (e.g. planes).
     */
    AABB getBoundingBox();
}