package com.raytracer;

import java.util.ArrayList;
import java.util.List;

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
}
