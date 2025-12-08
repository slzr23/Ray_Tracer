package com.raytracer;
import com.imaging.Color;
import com.geometry.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.geometry.Point;
import com.geometry.Shape;

public class RayTracer {
    private Scene scene;
    private static final double EPSILON = 1e-4;
    private final BVHNode bvhRoot;
    private final List<Shape> unboundedShapes;

    public RayTracer(Scene scene) {
        this.scene = scene;
        List<Shape> bounded = new ArrayList<>();
        List<Shape> unbounded = new ArrayList<>();
        for (Shape shape : scene.getShapes()) {
            if (shape.getBoundingBox() != null) {
                bounded.add(shape);
            } else {
                unbounded.add(shape);
            }
        }
        this.bvhRoot = BVHNode.build(bounded);
        this.unboundedShapes = unbounded;
    }

    public Color getPixelColor(int i, int j) {
        Ray primaryRay = generateRay(i, j);
        Optional<Intersection> intersection = findClosestIntersection(primaryRay);

        if (intersection.isPresent()) {
            return computeColor(intersection.get(), primaryRay, scene.getMaxDepth());
        }
        return new Color(0f, 0f, 0f);
    }

    /**
     * Calcule la couleur d'un point en fonction de l'illumination
     * @param intersection l'intersection avec l'objet
     * @return la couleur calculée
     */
    private Color computeColor(Intersection intersection, Ray incomingRay, int remainingDepth) {
        // Récupérer les informations de l’intersection
        Shape shape = intersection.getShape();
        Point point = intersection.getPoint();

        // Normale au point d’intersection
        Vector normal = shape.getNormalAt(point).normalize();

        // Oriente la normale pour qu'elle fasse face au rayon incident (évite un rebond vers l'intérieur)
        if (normal.dot(incomingRay.getDirection()) > 0) {
            normal = normal.scale(-1);
        }

        // Composante ambiante
        Color ambient = scene.getAmbient();
        float r = ambient.getR();
        float g = ambient.getG();
        float b = ambient.getB();

        // Direction de vue (du point vers la caméra)
        Camera camera = scene.getCamera();
        Point eye = camera.getPosition();
        Vector viewDir = new Vector(
            eye.getX() - point.getX(),
            eye.getY() - point.getY(),
            eye.getZ() - point.getZ()
        ).normalize();

        // (Double face géré par lumière ci-dessous)

        // Contribution de chaque lumière (Lambert + Blinn-Phong + ombres)
        for (Light light : scene.getLights()) {
            Vector lightDir;
            double maxDistance = Double.POSITIVE_INFINITY; // utile pour les PointLight

            if (light instanceof DirectionalLight) {
                DirectionalLight dirLight = (DirectionalLight) light;
                lightDir = dirLight.getDirection().normalize();
            } else if (light instanceof PointLight) {
                PointLight pointLight = (PointLight) light;
                Point lightPos = pointLight.getPosition();

                double dx = lightPos.getX() - point.getX();
                double dy = lightPos.getY() - point.getY();
                double dz = lightPos.getZ() - point.getZ();

                maxDistance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                lightDir = new Vector(dx, dy, dz).normalize();
            } else {
                continue; // Type de lumière non supporté
            }

            // ================== TEST D’OMBRE ==================
            // On décale légèrement l’origine le long de la normale pour éviter 
            // de se réintersecter avec l'objet lui-même.
            Point shadowOrigin = new Point(
                point.getX() + normal.getX() * EPSILON,
                point.getY() + normal.getY() * EPSILON,
                point.getZ() + normal.getZ() * EPSILON
            );
            Ray shadowRay = new Ray(shadowOrigin, lightDir);

            Optional<Intersection> shadowHit = findClosestIntersection(shadowRay);
            boolean inShadow = false;

            if (shadowHit.isPresent()) {
                double tShadow = shadowHit.get().getT();

                if (light instanceof PointLight) {
                    // L’objet doit être entre le point et la lumière
                    if (tShadow > EPSILON && tShadow < maxDistance - EPSILON) {
                        inShadow = true;
                    }
                } else { // DirectionalLight
                    if (tShadow > EPSILON) {
                        inShadow = true;
                    }
                }
            }

            // Si ce point est dans l’ombre pour cette lumière → pas de diffuse ni specular
            if (inShadow) {
                continue;
            }

            // ================== DIFFUSE (Lambert) ==================
            // Autoriser double-face : si la normale est tournée à l'opposé de la lumière, on la retourne pour ce calcul
            Vector lightNormal = normal;
            double dotNL = lightNormal.dot(lightDir);
            if (dotNL < 0.0) {
                lightNormal = lightNormal.scale(-1);
                dotNL = -dotNL;
            }

            double diffuseIntensity = dotNL; // normales et lightDir sont normalisés
            Color lightColor = light.getColor();
            Color diffuseColor = shape.getDiffuse();
            Color specularColor = shape.getSpecular();   // à avoir dans Sphere
            float shininess = shape.getShininess();      // pareil

            r += (float) (diffuseIntensity * lightColor.getR() * diffuseColor.getR());
            g += (float) (diffuseIntensity * lightColor.getG() * diffuseColor.getG());
            b += (float) (diffuseIntensity * lightColor.getB() * diffuseColor.getB());

            // ================== SPECULAIRE (Blinn-Phong) ==================
            // h = (lightDir + viewDir) / ||lightDir + viewDir||
            Vector h = lightDir.add(viewDir).normalize();
            double dotNH = Math.max(lightNormal.dot(h), 0.0);
            double specIntensity = Math.pow(dotNH, shininess);

            if (specIntensity > 0.0) {
                r += (float) (specIntensity * lightColor.getR() * specularColor.getR());
                g += (float) (specIntensity * lightColor.getG() * specularColor.getG());
                b += (float) (specIntensity * lightColor.getB() * specularColor.getB());
            }
        }

        // ================== REFLEXION (bonus) ==================
        boolean hasSpecular = shape.getSpecular() != null && (
            shape.getSpecular().getR() > 0f || shape.getSpecular().getG() > 0f || shape.getSpecular().getB() > 0f
        );

        if (hasSpecular && remainingDepth > 0) {
            Vector d = incomingRay.getDirection();
            // r = d - 2*(d·n)*n (n déjà orientée vers l'extérieur par rapport au rayon)
            Vector reflectDir = d.subtract(normal.scale(2 * d.dot(normal))).normalize();

            Point reflectOrigin = new Point(
                point.getX() + normal.getX() * EPSILON,
                point.getY() + normal.getY() * EPSILON,
                point.getZ() + normal.getZ() * EPSILON
            );

            Ray reflectRay = new Ray(reflectOrigin, reflectDir);
            Optional<Intersection> reflectHit = findClosestIntersection(reflectRay);

            if (reflectHit.isPresent()) {
                Color reflected = computeColor(reflectHit.get(), reflectRay, remainingDepth - 1);
                Color spec = shape.getSpecular();

                r += reflected.getR() * spec.getR();
                g += reflected.getG() * spec.getG();
                b += reflected.getB() * spec.getB();
            }
        }

        // Clamp [0,1]
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

            if (bvhRoot != null) {
                Optional<Intersection> hit = bvhRoot.intersect(ray, minDistance);
                if (hit.isPresent() && hit.get().getT() > EPSILON) {
                    closest = hit.get();
                    minDistance = closest.getT();
                }
            }

            for (Shape shape : unboundedShapes) {
                Optional<Intersection> intersection = shape.intersect(ray);
                if (intersection.isPresent()) {
                    Intersection inter = intersection.get();
                    double t = inter.getT();
                    if (t > EPSILON && t < minDistance) {
                        minDistance = t;
                        closest = inter;
                    }
                }
            }

            return Optional.ofNullable(closest);
        }
}
