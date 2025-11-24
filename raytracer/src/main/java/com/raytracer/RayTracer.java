package com.raytracer;
import com.imaging.Color;
import com.geometry.Vector;

import java.util.Optional;

import com.geometry.Point;
import com.geometry.Shape;
import com.geometry.Sphere;

public class RayTracer {
    private Scene scene;
    public RayTracer(Scene scene) {
        this.scene = scene;
    }

    public Color getPixelColor(int i, int j) {
    // Générele rayon pour ce pixel
    Ray ray = generateRay(i, j);
    
    // Trouve l'intersection la plus proche
    Optional<Intersection> intersection = findClosestIntersection(ray);
    
    // Calculer la couleur
    if (intersection.isPresent()) {
        // y a une intersection : utiliser la couleur ambiante
        return scene.getAmbient();
    } else {
        // Pas d'intersection : pixel noir/couleur fond
        return new Color(0f, 0f, 0f);
    }
}

        /**Génère rayon pour pixel (i, j)**/
    public Ray generateRay(int i, int j) {
        Camera camera = scene.getCamera();
        int imgWidth = scene.getWidth();
        int imgHeight = scene.getHeight();
        
        //repère orthonormé
        Orthonormal ortho = camera.getOrthonormal();
        Vector u = ortho.getU();
        Vector v = ortho.getV();
        Vector w = ortho.getW();
        
        //dimensions des pixels
        double pixelHeight = camera.getPixelHeight();
        double pixelWidth = camera.getPixelWidth(imgWidth, imgHeight);
        
        // Convertir (i, j) en coord (a, b)
        double a = (pixelWidth * (i - imgWidth/2.0 + 0.5)) / (imgWidth/2.0);
        double b = (pixelHeight * (j - imgHeight/2.0 + 0.5)) / (imgHeight/2.0);
        
        // Calculer vecteur direction : d = u*a + v*b - w
        Vector direction = u.scale(a)
                            .add(v.scale(b))
                            .subtract(w)
                            .normalize();
        
        // Créer rayon depuis camera
        return new Ray(camera.getPosition(), direction);
    }

        /**
         * Trouve l'intersection la plus proche le long d'un rayon
         * @param ray le rayon à tester
         * @return Optional contenant l'intersection la plus proche, ou empty si aucune
         */
        public Optional<Intersection> findClosestIntersection(Ray ray) {
            Intersection closest = null;
            double minDistance = Double.POSITIVE_INFINITY;
            
            // Parcourir tous les objets de la scène
            for (Shape shape : scene.getShapes()) {
                // que les sphères pour l'instant
                if (shape instanceof Sphere) {
                    Sphere sphere = (Sphere) shape;
                    Optional<Intersection> intersection = sphere.intersect(ray);
                    
                    // Si intersection trouvée
                    if (intersection.isPresent()) {
                        Intersection inter = intersection.get();
                        double t = inter.getT();
                        
                        // Garder seulement si c'est la plus proche (et devant la caméra)
                        if (t > 0 && t < minDistance) {
                            minDistance = t;
                            closest = inter;
                        }
                    }
                }
            }
            
            return Optional.ofNullable(closest);
        }
}
