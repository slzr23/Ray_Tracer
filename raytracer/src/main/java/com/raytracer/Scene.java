package com.raytracer;

public class Scene {
    private int width;
    private int height;
    private String outputFile = "output.png";
    private Camera camera;

    public Scene(int width, int height, String outputFile, Camera camera) {
        this.width = width;
        this.height = height;
        this.outputFile = outputFile;
        this.camera = camera;
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
