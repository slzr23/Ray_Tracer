package com.geometry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.geometry.Point;
import com.geometry.Vector;

public class PointTest {

    @Test
    void testSubtractToVector() {
        Point p1 = new Point(2.0, 3.0, 5.0);
        Point p2 = new Point(1.5, -1.0, 2.0);
        Vector v = p1.subtract(p2);
        assertEquals(new Vector(0.5, 4.0, 3.0), v);
    }

    @Test
    void testScale() {
        Point p = new Point(1.0, -2.0, 3.0);
        Point q = p.scale(2.0);
        assertEquals(new Point(2.0, -4.0, 6.0), q);
    }

    @Test
    void testEqualsWithTolerance() {
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(1.0 + 1e-10, 2.0 - 1e-10, 3.0 + 1e-10);
        assertEquals(p1, p2);
    }
}
