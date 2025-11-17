package com.raytracer;

public interface Light {
    Color getColor();

    LightSample sampleAt(Point p);
}
