package com.raytracer;

import com.geometry.Point;
import com.imaging.Color;

public interface Light {
    Color getColor();

    LightSample sampleAt(Point p);
}
