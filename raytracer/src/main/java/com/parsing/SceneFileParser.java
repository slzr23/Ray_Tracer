package com.parsing;
import com.imaging.Color;
import com.raytracer.*;
import com.geometry.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Analyseur syntaxique pour les fichiers de description de scène.
 * <p>
 * Cette classe permet de charger une scène 3D à partir d'un fichier texte
 * suivant un format défini. Le parseur supporte la définition de :
 * </p>
 * <ul>
 *   <li>Dimensions de l'image de sortie et nom du fichier</li>
 *   <li>Paramètres de caméra (position, direction, champ de vision)</li>
 *   <li>Matériaux (couleurs diffuse, spéculaire, brillance)</li>
 *   <li>Sources lumineuses (directionnelles et ponctuelles)</li>
 *   <li>Primitives géométriques (sphères, triangles, plans)</li>
 * </ul>
 * <p>
 * Le format supporte également les commentaires (lignes commençant par #)
 * et gère la définition de vertex pour la création de maillages triangulaires.
 * </p>
 * 
 * @author Jules
 * @version 1.0
 * @see Scene
 */
public class SceneFileParser {

    private final Scene scene;
    private Color currentDiffuse = new Color(0f, 0f, 0f);
    private Color currentSpecular = new Color(0f, 0f, 0f);
    private float currentShininess = 0.0f;
    private Point[] vertices;      // stocke les vertex déclarés
    private int vertexCount = 0;   // nombre de vertex actuellement ajoutés 

    public SceneFileParser(Scene scene) {
        this.scene = scene;
    }

    public void parse(String filePath) throws IOException {

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // ignorer lignes vides / commentaires
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // découper la ligne
                String[] tokens = line.split("\\s+");
                String keyword = tokens[0];

                switch (keyword) {

                    case "size":
                        int width = Integer.parseInt(tokens[1]);
                        int height = Integer.parseInt(tokens[2]);
                        scene.setWidth(width);
                        scene.setHeight(height);
                        break;

                    case "output":
                        scene.setOutput(tokens[1]);
                        break;

                    case "camera":
                        double px = Double.parseDouble(tokens[1]); // position (x, y, z)
                        double py = Double.parseDouble(tokens[2]);
                        double pz = Double.parseDouble(tokens[3]);

                        double lx = Double.parseDouble(tokens[4]); // lookAt (u, v, w)
                        double ly = Double.parseDouble(tokens[5]);
                        double lz = Double.parseDouble(tokens[6]);

                        double ux = Double.parseDouble(tokens[7]); // up (m, n, o)
                        double uy = Double.parseDouble(tokens[8]);
                        double uz = Double.parseDouble(tokens[9]);

                        double fov = Double.parseDouble(tokens[10]); // f

                        Point position = new Point(px, py, pz);
                        Point lookAt   = new Point(lx, ly, lz);
                        Vector up      = new Vector(ux, uy, uz);

                        Camera camera = new Camera(position, lookAt, up, fov);
                        scene.setCamera(camera);
                        break;

                    case "maxdepth":
                        int md = Integer.parseInt(tokens[1]);
                        if (md < 0) {
                            throw new IllegalArgumentException("maxdepth doit être >= 0");
                        }
                        scene.setMaxDepth(md);
                        break;

                    case "ambient": 
                        float ambR = Float.parseFloat(tokens[1]);
                        float ambG = Float.parseFloat(tokens[2]);
                        float ambB = Float.parseFloat(tokens[3]);

                        Color ambient = new Color(ambR, ambG, ambB);

                        if (ambient.getR() + currentDiffuse.getR() > 1.0f ||
                            ambient.getG() + currentDiffuse.getG() > 1.0f ||
                            ambient.getB() + currentDiffuse.getB() > 1.0f) {
                            throw new IllegalArgumentException("ambient + diffuse dépasse 1");
                        }

                        scene.setAmbient(ambient);
                        break;

                    case "diffuse": 
                        float difR = Float.parseFloat(tokens[1]);
                        float difG = Float.parseFloat(tokens[2]);
                        float difB = Float.parseFloat(tokens[3]);

                        Color diffuse = new Color(difR, difG, difB);

                        Color CurrentAmbient = scene.getAmbient();
                        if (CurrentAmbient != null) {
                            if (CurrentAmbient.getR() + diffuse.getR() > 1.0f ||
                                CurrentAmbient.getG() + diffuse.getG() > 1.0f ||
                                CurrentAmbient.getB() + diffuse.getB() > 1.0f) {
                                throw new IllegalArgumentException("ambient + diffuse dépasse 1");
                            }
                        }

                        currentDiffuse = diffuse;
                        break;
                    
                    case "specular":
                        float specR = Float.parseFloat(tokens[1]);
                        float specG = Float.parseFloat(tokens[2]);
                        float specB = Float.parseFloat(tokens[3]);

                        Color specular = new Color(specR, specG, specB);
                        currentSpecular = specular;
                        break;

                    case "shininess":
                        float shininess = Float.parseFloat(tokens[1]);
                        if (shininess < 0) {
                            shininess = 0.0f; // on borne à 0 pour éviter les échecs de parsing sur certaines scènes
                        }
                        currentShininess = shininess;
                        break;

                    case "directional":
                        // directional dx dy dz r g b
                        double dirX = Double.parseDouble(tokens[1]);
                        double dirY = Double.parseDouble(tokens[2]);
                        double dirZ = Double.parseDouble(tokens[3]);
                        float lightDirR = Float.parseFloat(tokens[4]);
                        float lightDirG = Float.parseFloat(tokens[5]);
                        float lightDirB = Float.parseFloat(tokens[6]);
                        
                        Vector direction = new Vector(dirX, dirY, dirZ).normalize();
                        Color lightDirColor = new Color(lightDirR, lightDirG, lightDirB);
                        DirectionalLight directionalLight = new DirectionalLight(direction, lightDirColor);
                        scene.getLights().add(directionalLight);
                        break;

                    case "point":
                        // point px py pz r g b
                        double lightPx = Double.parseDouble(tokens[1]);
                        double lightPy = Double.parseDouble(tokens[2]);
                        double lightPz = Double.parseDouble(tokens[3]);
                        float lightPointR = Float.parseFloat(tokens[4]);
                        float lightPointG = Float.parseFloat(tokens[5]);
                        float lightPointB = Float.parseFloat(tokens[6]);
                        
                        Point lightPosition = new Point(lightPx, lightPy, lightPz);
                        Color lightPointColor = new Color(lightPointR, lightPointG, lightPointB);
                        PointLight pointLight = new PointLight(lightPosition, lightPointColor);
                        scene.getLights().add(pointLight);
                        break;

                    case "sphere":
                        double cx = Double.parseDouble(tokens[1]); // centre (x, y, z)
                        double cy = Double.parseDouble(tokens[2]);
                        double cz = Double.parseDouble(tokens[3]);
                        double radius = Double.parseDouble(tokens[4]); // r

                        Point center = new Point(cx, cy, cz);

                        Sphere sphere = new Sphere(center, radius, currentDiffuse, currentSpecular, currentShininess);
                        scene.getShapes().add(sphere);
                        break;

                    case "maxverts":
                        int maxVerts = Integer.parseInt(tokens[1]);
                        vertices = new Point[maxVerts];
                        vertexCount = 0;
                        break;

                    case "vertex":
                        if (vertices == null) {
                            throw new IllegalStateException("maxverts doit être déclaré avant vertex");
                        }
                        if (vertexCount >= vertices.length) {
                            throw new IllegalArgumentException("Trop de vertex déclarés (max = " + vertices.length + ")");
                        }
                        double vx = Double.parseDouble(tokens[1]);
                        double vy = Double.parseDouble(tokens[2]);
                        double vz = Double.parseDouble(tokens[3]);
                        vertices[vertexCount++] = new Point(vx, vy, vz);
                        break;

                    case "tri":
                        if (vertices == null) { // on verif que maxverts a été déclaré
                            throw new IllegalStateException("maxverts doit être déclaré avant tri");
                        }
                        int idx1 = Integer.parseInt(tokens[1]);
                        int idx2 = Integer.parseInt(tokens[2]);
                        int idx3 = Integer.parseInt(tokens[3]);
                        if (idx1 < 0 || idx1 >= vertexCount ||
                            idx2 < 0 || idx2 >= vertexCount ||
                            idx3 < 0 || idx3 >= vertexCount) {
                            throw new IndexOutOfBoundsException("Index de vertex invalide dans tri");
                        }
                        Triangle triangle = new Triangle(vertices[idx1], vertices[idx2], vertices[idx3], currentDiffuse, currentSpecular, currentShininess);
                        scene.getShapes().add(triangle);
                        break;

                    case "plane":
                        double planePx = Double.parseDouble(tokens[1]); // point (x, y, z)
                        double planePy = Double.parseDouble(tokens[2]);
                        double planePz = Double.parseDouble(tokens[3]);
                        double planeNx = Double.parseDouble(tokens[4]); // normale (u, v, w)
                        double planeNy = Double.parseDouble(tokens[5]);
                        double planeNz = Double.parseDouble(tokens[6]);
                        Point planePoint = new Point(planePx, planePy, planePz);
                        Vector planeNormal = new Vector(planeNx, planeNy, planeNz);
                        Plane plane = new Plane(planePoint, planeNormal, currentDiffuse, currentSpecular, currentShininess);
                        scene.getShapes().add(plane);
                        break;

                    default:
                        System.out.println("Mot clé ignoré : " + keyword);
                }
            }
        }
    }
}
