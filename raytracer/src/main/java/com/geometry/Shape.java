package com.geometry;

import com.imaging.Color;

public interface Shape {
    Color getDiffuse();
    Color getSpecular();
    Float getShininess();
}