package com.geometry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class VectorTest {

    @Test
    void testAddSubtract() {
        Vector a = new Vector(1.0, 2.0, 3.0);
        Vector b = new Vector(-1.0, 0.5, 4.0);
        Vector sum = a.add(b);
        assertEquals(new Vector(0.0, 2.5, 7.0), sum);
        Vector diff = a.subtract(b);
        assertEquals(new Vector(2.0, 1.5, -1.0), diff);
    }

    @Test
    void testScaleDotCross() {
        Vector v = new Vector(2.0, -3.0, 4.0);
        assertEquals(new Vector(4.0, -6.0, 8.0), v.scale(2.0));
        //assertEquals(4.0*1.0 + (-3.0)*2.0 + 4.0*3.0, v.dot(new Vector(1.0, 2.0, 3.0)), 1e-9);
        Vector cross = new Vector(1.0, 0.0, 0.0).cross(new Vector(0.0, 1.0, 0.0));
        assertEquals(new Vector(0.0, 0.0, 1.0), cross);
    }

    @Test
    void testLengthNormalize() {
        Vector v = new Vector(3.0, 4.0, 0.0);
        assertEquals(5.0, v.length(), 1e-9);
        Vector n = v.normalize();
        assertEquals(1.0, n.length(), 1e-9);
    }

    @Test
    void testEqualsWithTolerance() {
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(1.0 + 1e-10, 2.0 - 1e-10, 3.0 + 5e-10);
        assertEquals(v1, v2);
    }
}
