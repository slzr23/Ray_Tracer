package com.raytracer;

import java.util.ArrayList;
import java.util.List;

import com.geometry.Shape;
import com.imaging.Color;

/**
 * Conteneur pour tous les éléments d'une scène 3D.
 * <p>
 * Stocke les dimensions de l'image, la caméra, les sources lumineuses,
 * les objets géométriques et les paramètres de rendu (couleur ambiante, 
 * profondeur de réflexion).
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public class Scene {
    private int width;
    private int height;
    private String outputFile = "output.png";
    private Camera camera;
    private Color ambient = new Color();
    private List<Light> lights = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
    private int maxDepth = 0; // profondeur de récursion pour la réflexion

    public Scene() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public Camera getCamera() {
        return camera;
    }

    public Color getAmbient() {
        return ambient;
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public int getMaxDepth() {
        return maxDepth;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setOutput(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setAmbient(Color ambient) {
        this.ambient = ambient;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = Math.max(0, maxDepth);
    }

}
