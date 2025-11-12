package com.raytracer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ColorTest {

    @Test
    void testDefaultBlack() {
        Color c = new Color();
        assertEquals(new Color(), c);
        assertEquals(0x000000, c.toRGB());
    }

    @Test
    void testAddScaleSchur() {
        Color a = new Color();
        a.setR(0.2); a.setG(0.3); a.setB(0.4);
        Color b = new Color();
        b.setR(0.5); b.setG(0.6); b.setB(0.7);

        Color sum = a.add(b);
        assertEquals(0.7, sum.getR(), 1e-9);
        assertEquals(0.9, sum.getG(), 1e-9);
        assertEquals(1.1, sum.getB(), 1e-9);

        Color scaled = a.scale(2.0);
        assertEquals(0.4, scaled.getR(), 1e-9);
        assertEquals(0.6, scaled.getG(), 1e-9);
        assertEquals(0.8, scaled.getB(), 1e-9);

        Color schur = a.schurProduct(b);
        assertEquals(0.1, schur.getR(), 1e-9);
        assertEquals(0.18, schur.getG(), 1e-9);
        assertEquals(0.28, schur.getB(), 1e-9);
    }

    @Test
    void testToRGBClamps() {
        Color c = new Color();
        c.setR(1.5); c.setG(-0.1); c.setB(0.5);
        int rgb = c.toRGB();
        // After clamping: (1.0, 0.0, 0.5) => (255, 0, 128) => 0xFF0080
        assertEquals(0xFF0080, rgb);
    }

    @Test
    void testEqualsWithTolerance() {
        Color c1 = new Color();
        c1.setR(0.2); c1.setG(0.3); c1.setB(0.4);
        Color c2 = new Color();
        c2.setR(0.20000000005); c2.setG(0.30000000005); c2.setB(0.39999999995);
        assertEquals(c1, c2);
    }
}
