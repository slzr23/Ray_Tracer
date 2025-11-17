package com.raytracer;

public class Scene {
    private int width;
    private int height;
    private String outputFile = "output.png";

    public Scene(int width, int height, String outputFile) {
        this.width = width;
        this.height = height;
        this.outputFile = outputFile;
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
    
    public Camera setCamera(Point position, Point lookAt, Vector up, double fov) {
        return new Camera(position, lookAt, up, fov);
    }
}
