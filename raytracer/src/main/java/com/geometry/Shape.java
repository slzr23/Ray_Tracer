package com.geometry;

import java.util.Optional;
import com.raytracer.AABB;
import com.raytracer.Intersection;
import com.raytracer.Ray;
import com.imaging.Color;

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