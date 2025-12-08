package com.raytracer;
import com.geometry.Vector;

/**
 * Base orthonormée (u, v, w) pour le repère caméra.
 * <p>
 * Utilisée pour convertir les coordonnées pixel en directions de rayons.
 * u = droite, v = haut, w = direction de visée (opposée à la direction de vue).
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public class Orthonormal {
    private final Vector u;  // Vecteur "droite"
    private final Vector v;  // Vecteur "haut"
    private final Vector w;  // Vecteur "devant"

    public Orthonormal(Vector u, Vector v, Vector w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }
    
    public Vector getU() {
        return u;
    }
    
    public Vector getV() {
        return v;
    }
    
    public Vector getW() {
        return w;
    }
}
