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

    public Scene(int width, int height, String outputFile, Camera camera, Color ambient, List<Light> lights, List<Shape> shapes) {
        this.width = width;
        this.height = height;
        this.outputFile = outputFile;
        this.camera = camera;
        this.ambient = ambient;
        this.lights = lights;
        this.shapes = shapes;
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

}
