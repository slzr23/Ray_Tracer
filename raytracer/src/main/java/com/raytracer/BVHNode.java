package com.raytracer;

import com.geometry.Shape;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Noeud d'une structure BVH (Bounding Volume Hierarchy) pour l'accélération
 * des tests d'intersection.
 * <p>
 * Cette classe implémente un arbre binaire où chaque noeud interne contient
 * une boite englobante (AABB) qui englobe tous ses enfants, et chaque feuille
 * contient une unique forme géométrique. La construction de l'arbre utilise
 * une heuristique de partitionnement basée sur l'axe de plus grande étendue.
 * </p>
 * <p>
 * L'utilisation d'une BVH permet de réduire la complexité des tests
 * d'intersection de O(n) à O(log n) en moyenne, où n est le nombre
 * de primitives dans la scène.
 * </p>
 * 
 * @author Jules
 * @version 1.0
 * @see AABB
 * @see Shape
 */
public class BVHNode {
    private final AABB box;
    private final BVHNode left;
    private final BVHNode right;
    private final Shape leafShape;

    private BVHNode(AABB box, BVHNode left, BVHNode right, Shape leafShape) {
        this.box = box;
        this.left = left;
        this.right = right;
        this.leafShape = leafShape;
    }

    public static BVHNode build(List<Shape> shapes) {
        if (shapes == null || shapes.isEmpty()) {
            return null;
        }
        // Defensive copy
        List<Shape> list = new ArrayList<>(shapes);
        return buildRecursive(list);
    }

    private static BVHNode buildRecursive(List<Shape> shapes) {
        if (shapes.size() == 1) {
            Shape s = shapes.get(0);
            return new BVHNode(s.getBoundingBox(), null, null, s);
        }

        // Compute global bbox and choose split axis by largest extent
        AABB global = shapes.get(0).getBoundingBox();
        double minX = global.getMin().getX();
        double minY = global.getMin().getY();
        double minZ = global.getMin().getZ();
        double maxX = global.getMax().getX();
        double maxY = global.getMax().getY();
        double maxZ = global.getMax().getZ();

        for (int i = 1; i < shapes.size(); i++) {
            AABB b = shapes.get(i).getBoundingBox();
            minX = Math.min(minX, b.getMin().getX());
            minY = Math.min(minY, b.getMin().getY());
            minZ = Math.min(minZ, b.getMin().getZ());
            maxX = Math.max(maxX, b.getMax().getX());
            maxY = Math.max(maxY, b.getMax().getY());
            maxZ = Math.max(maxZ, b.getMax().getZ());
        }
        AABB globalBox = new AABB(new com.geometry.Point(minX, minY, minZ), new com.geometry.Point(maxX, maxY, maxZ));

        double extentX = maxX - minX;
        double extentY = maxY - minY;
        double extentZ = maxZ - minZ;

        int axis;
        if (extentX >= extentY && extentX >= extentZ) {
            axis = 0;
        } else if (extentY >= extentX && extentY >= extentZ) {
            axis = 1;
        } else {
            axis = 2;
        }

        // Sort by centroid along chosen axis
        Comparator<Shape> cmp;
        switch (axis) {
            case 0:
                cmp = Comparator.comparingDouble(s -> (s.getBoundingBox().getMin().getX() + s.getBoundingBox().getMax().getX()) * 0.5);
                break;
            case 1:
                cmp = Comparator.comparingDouble(s -> (s.getBoundingBox().getMin().getY() + s.getBoundingBox().getMax().getY()) * 0.5);
                break;
            default:
                cmp = Comparator.comparingDouble(s -> (s.getBoundingBox().getMin().getZ() + s.getBoundingBox().getMax().getZ()) * 0.5);
        }
        listSort(shapes, cmp);

        int mid = shapes.size() / 2;
        List<Shape> leftList = shapes.subList(0, mid);
        List<Shape> rightList = shapes.subList(mid, shapes.size());

        BVHNode leftNode = buildRecursive(new ArrayList<>(leftList));
        BVHNode rightNode = buildRecursive(new ArrayList<>(rightList));

        return new BVHNode(globalBox, leftNode, rightNode, null);
    }

    private static <T> void listSort(List<T> list, Comparator<? super T> c) {
        list.sort(c);
    }

    public Optional<Intersection> intersect(Ray ray, double currentClosest) {
        if (!box.intersects(ray, currentClosest)) {
            return Optional.empty();
        }
        if (leafShape != null) {
            return leafShape.intersect(ray);
        }
        Intersection hitLeft = null;
        Intersection hitRight = null;
        double closest = currentClosest;

        if (left != null) {
            Optional<Intersection> l = left.intersect(ray, closest);
            if (l.isPresent()) {
                hitLeft = l.get();
                closest = hitLeft.getT();
            }
        }
        if (right != null) {
            Optional<Intersection> r = right.intersect(ray, closest);
            if (r.isPresent()) {
                hitRight = r.get();
                if (hitRight.getT() < closest) {
                    closest = hitRight.getT();
                }
            }
        }
        if (hitLeft != null && hitRight != null) {
            return Optional.of(hitLeft.getT() <= hitRight.getT() ? hitLeft : hitRight);
        }
        if (hitLeft != null) return Optional.of(hitLeft);
        if (hitRight != null) return Optional.of(hitRight);
        return Optional.empty();
    }
}
