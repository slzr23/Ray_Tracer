package com.raytracer;

import com.geometry.Point;
import com.geometry.Vector;

/**
 * Boite englobante alignée sur les axes (Axis-Aligned Bounding Box).
 * <p>
 * Une AABB est définie par deux points représentant les coins opposés
 * de la boite : le coin minimal et le coin maximal. Cette structure
 * est utilisée pour accélérer les calculs d'intersection en permettant
 * d'éliminer rapidement les objets qui ne peuvent pas être touchés
 * par un rayon donné.
 * </p>
 * <p>
 * L'algorithme d'intersection utilise la méthode des slabs (tranches)
 * qui teste l'entrée et la sortie du rayon pour chaque paire de plans
 * parallèles aux axes.
 * </p>
 * 
 * @author Jules
 * @version 1.0
 * @see BVHNode
 */
public class AABB {
    private final Point min;
    private final Point max;

    public AABB(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    public Point getMin() {
        return min;
    }

    public Point getMax() {
        return max;
    }

    /**
     * Intersects a ray with the box using the slabs method.
     * @param ray incoming ray
     * @param tMaxCap optional maximum distance (e.g. current closest hit); pass Double.POSITIVE_INFINITY if none
     * @return true if the ray intersects the box within [0, tMaxCap]
     */
    public boolean intersects(Ray ray, double tMaxCap) {
        Vector dir = ray.getDirection();
        Point orig = ray.getOrigin();

        double tMin = 0.0;
        double tMax = tMaxCap;

        double[] o = {orig.getX(), orig.getY(), orig.getZ()};
        double[] d = {dir.getX(), dir.getY(), dir.getZ()};
        double[] mn = {min.getX(), min.getY(), min.getZ()};
        double[] mx = {max.getX(), max.getY(), max.getZ()};

        for (int axis = 0; axis < 3; axis++) {
            double invD = 1.0 / d[axis];
            double t0 = (mn[axis] - o[axis]) * invD;
            double t1 = (mx[axis] - o[axis]) * invD;
            if (invD < 0.0) {
                double tmp = t0;
                t0 = t1;
                t1 = tmp;
            }
            tMin = Math.max(tMin, t0);
            tMax = Math.min(tMax, t1);
            if (tMax < tMin) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a bounding box enclosing both this and another box.
     */
    public AABB union(AABB other) {
        Point nMin = new Point(
            Math.min(min.getX(), other.min.getX()),
            Math.min(min.getY(), other.min.getY()),
            Math.min(min.getZ(), other.min.getZ())
        );
        Point nMax = new Point(
            Math.max(max.getX(), other.max.getX()),
            Math.max(max.getY(), other.max.getY()),
            Math.max(max.getZ(), other.max.getZ())
        );
        return new AABB(nMin, nMax);
    }
}
