package com.raytracer;

public abstract class AbstractLight implements Light {
    protected final Color color;

    public AbstractLight(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public abstract LightSample sampleAt(Point p);
}
