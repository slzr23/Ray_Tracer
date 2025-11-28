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
        // Générer le rayon pour ce pixel
        Ray ray = generateRay(i, j);
        
        // Trouver l'intersection la plus proche
        Optional<Intersection> intersection = findClosestIntersection(ray);
        
        // Calculer la couleur
        if (intersection.isPresent()) {
            // Il y a une intersection : calculer l'illumination
            return computeColor(intersection.get());
        } else {
            // Pas d'intersection : pixel noir/couleur fond
            return new Color(0f, 0f, 0f);
        }
    }

    /**
     * Calcule la couleur d'un point en fonction de l'illumination
     * @param intersection l'intersection avec l'objet
     * @return la couleur calculée
     */
    private Color computeColor(Intersection intersection) {
        // Récupérer les informations de l'intersection
        Shape shape = intersection.getShape();
        Point point = intersection.getPoint();
        
        // Pour l'instant, on ne gère que les sphères
        if (!(shape instanceof Sphere)) {
            return scene.getAmbient(); // Retour par défaut pour les autres formes
        }
        
        Sphere sphere = (Sphere) shape;
        
        // Calculer la normale au point d'intersection
        Vector normal = sphere.getNormalAt(point);
        
        // Commencer avec la lumière ambiante
        Color ambient = scene.getAmbient();
        float r = ambient.getR();
        float g = ambient.getG();
        float b = ambient.getB();
        
        // Ajouter la contribution de chaque lumière (réflexion diffuse de Lambert)
        for (Light light : scene.getLights()) {
            Vector lightDir;
            
            if (light instanceof DirectionalLight) {
                // Lumière directionnelle : direction constante
                DirectionalLight dirLight = (DirectionalLight) light;
                lightDir = dirLight.getDirection(); // Direction vers la lumière
            } else if (light instanceof PointLight) {
                // Lumière ponctuelle : direction du point vers la lumière
                PointLight pointLight = (PointLight) light;
                Point lightPos = pointLight.getPosition();
                lightDir = new Vector(
                    lightPos.getX() - point.getX(),
                    lightPos.getY() - point.getY(),
                    lightPos.getZ() - point.getZ()
                ).normalize();
            } else {
                continue; // Type de lumière non supporté
            }
            
            // Formule de Lambert : ld = max(n · lightdir, 0) * lightcolor * colordiffuse
            double dotProduct = normal.dot(lightDir);
            double intensity = Math.max(dotProduct, 0.0);
            
            if (intensity > 0) {
                Color lightColor = light.getColor();
                Color diffuseColor = sphere.getDiffuse();
                
                r += (float) (intensity * lightColor.getR() * diffuseColor.getR());
                g += (float) (intensity * lightColor.getG() * diffuseColor.getG());
                b += (float) (intensity * lightColor.getB() * diffuseColor.getB());
            }
        }
        
        // Clamper les valeurs entre 0 et 1
        r = Math.min(1.0f, Math.max(0.0f, r));
        g = Math.min(1.0f, Math.max(0.0f, g));
        b = Math.min(1.0f, Math.max(0.0f, b));
        
        return new Color(r, g, b);
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
        double b = -(pixelHeight * (j - imgHeight/2.0 + 0.5)) / (imgHeight/2.0); //inversion des données verticales (spécifiées dans l'énoncé)
        
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
