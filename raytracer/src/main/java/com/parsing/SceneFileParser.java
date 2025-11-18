package com.parsing;
import com.imaging.Color;
import com.raytracer.*;
import com.geometry.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SceneFileParser {

    private final Scene scene;
    private Color currentDiffuse = new Color(0f, 0f, 0f);
    private Color currentSpecular = new Color(0f, 0f, 0f);
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
                                if (CurrentAmbient.getR() + currentDiffuse.getR() > 1.0f ||
                                    CurrentAmbient.getG() + currentDiffuse.getG() > 1.0f ||
                                    CurrentAmbient.getB() + currentDiffuse.getB() > 1.0f) {
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

                    case "sphere":
                        double cx = Double.parseDouble(tokens[1]); // centre (x, y, z)
                        double cy = Double.parseDouble(tokens[2]);
                        double cz = Double.parseDouble(tokens[3]);
                        double radius = Double.parseDouble(tokens[4]); // r

                        Point center = new Point(cx, cy, cz);

                        Sphere sphere = new Sphere(center, radius, currentDiffuse, currentSpecular);
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
                        
                        Triangle triangle = new Triangle(vertices[idx1], vertices[idx2], vertices[idx3], currentDiffuse, currentSpecular);
                        
                        scene.getShapes().add(triangle);
                        break;

                    default:
                        System.out.println("Mot clé ignoré : " + keyword);
                }
            }
        }
    }
}
