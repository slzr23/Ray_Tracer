package com.imaging;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.imaging.Color;

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
        a.setR(0.2f); a.setG(0.3f); a.setB(0.4f);
        Color b = new Color();
        b.setR(0.5f); b.setG(0.6f); b.setB(0.7f);

        Color sum = a.add(b);
        assertEquals(0.7f, sum.getR(), 1e-9);
        assertEquals(0.9f, sum.getG(), 1e-9);
        assertEquals(1.1f, sum.getB(), 1e-9);

        Color scaled = a.scale(2.0f);
        assertEquals(0.4f, scaled.getR(), 1e-9);
        assertEquals(0.6f, scaled.getG(), 1e-9);
        assertEquals(0.8f, scaled.getB(), 1e-9);

        Color schur = a.schurProduct(b);
        assertEquals(0.1f, schur.getR(), 1e-9);
        assertEquals(0.18f, schur.getG(), 1e-9);
        assertEquals(0.28f, schur.getB(), 1e-9);
    }

    @Test
    void testToRGBClamps() {
        Color c = new Color();
        c.setR(1.5f); c.setG(-0.1f); c.setB(0.5f);
        int rgb = c.toRGB();
        // After clamping: (1.0, 0.0, 0.5) => (255, 0, 128) => 0xFF0080
        assertEquals(0xFF0080, rgb);
    }

    @Test
    void testEqualsWithTolerance() {
        Color c1 = new Color();
        c1.setR(0.2f); c1.setG(0.3f); c1.setB(0.4f);
        Color c2 = new Color();
        c2.setR(0.20000000005f); c2.setG(0.30000000005f); c2.setB(0.39999999995f);
        assertEquals(c1, c2);
    }
}
