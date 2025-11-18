package com.imaging;

import com.raytracer.AbstractVec3;

public class Color {
    private float r;
    private float g;
    private float b;

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color() {
        this.r = 0.0f;
        this.g = 0.0f;
        this.b = 0.0f;
    }

    //Equals avec tolérance sur doubles
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Color color = (Color) obj;
        return AbstractVec3.nearlyEqual(color.r, r) &&
               AbstractVec3.nearlyEqual(color.g, g) &&
               AbstractVec3.nearlyEqual(color.b, b);
    }


    // Getters et Setters
    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public void setR(float r) {
        this.r = r;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setB(float b) {
        this.b = b;
    }

    // -----Opérations sur les couleurs-----
    // Addition
    public Color add(Color c) {
        Color ColorResult = new Color();
        ColorResult.setR(this.r + c.r);
        ColorResult.setG(this.g + c.g);
        ColorResult.setB(this.b + c.b);
        return ColorResult;
    }

    // Multiplication par un scalaire
    public Color scale(float scalar) {
        Color ColorResult = new Color();
        ColorResult.setR(this.r * scalar);
        ColorResult.setG(this.g * scalar);
        ColorResult.setB(this.b * scalar);
        return ColorResult;
    }

    // Produit de Schur
    public Color schurProduct(Color c) {
        Color ColorResult = new Color();
        ColorResult.setR(this.r * c.r);
        ColorResult.setG(this.g * c.g);
        ColorResult.setB(this.b * c.b);
        return ColorResult;
    }
    // Conversion RGB 
    public int toRGB(){
    // Clamp to [0,1] then convert
    double rc = AbstractVec3.clamp(r, 0.0, 1.0);
    double gc = AbstractVec3.clamp(g, 0.0, 1.0);
    double bc = AbstractVec3.clamp(b, 0.0, 1.0);

    int red = (int) Math.round(rc * 255.0);
    int green = (int) Math.round(gc * 255.0);
    int blue = (int) Math.round(bc * 255.0);

    return (
        ((red & 0xff) << 16)
        + ((green & 0xff) << 8)
        + (blue & 0xff));
    }
}
