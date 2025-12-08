package com.raytracer;

import java.util.ArrayList;
import java.util.List;

import com.geometry.Shape;
import com.imaging.Color;

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
