package com.parsing;

import org.junit.jupiter.api.Test;

import com.geometry.Point;
import com.raytracer.Scene;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

public class SceneFileParserTest {
    
    @Test
    public void testParseSimpleScene() throws Exception {
        Scene scene = new Scene();
        SceneFileParser parser = new SceneFileParser(scene);
        
        String testFilePath = Objects.requireNonNull(
            getClass().getClassLoader().getResource("scenes/tp31.test")
        ).getPath();

        parser.parse(testFilePath);

        assertEquals(800, scene.getWidth());
        assertEquals(600, scene.getHeight());
        assertEquals("output.png", scene.getOutputFile());
        
        assertNotNull(scene.getCamera());
        assertEquals(new Point(0, 0, -5), scene.getCamera().getPosition());
        assertEquals(new Point(0, 0, 0), scene.getCamera().getLookAt());
        
        assertEquals(1, scene.getShapes().size());

    }

}
