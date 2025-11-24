package com.raytracer;
import com.geometry.Vector;

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
