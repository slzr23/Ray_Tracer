package com.parsing;

import com.geometry.*;
import com.imaging.Color;
import com.raytracer.Scene;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SceneFileParserTest {

    // Test qui utilise tp31.test
    @Test
    public void testParseTp31Scene_basic() throws Exception {
        Scene scene = new Scene();
        SceneFileParser parser = new SceneFileParser(scene);

        String filePath = "src/test/java/com/ressources/scenes/tp31.test";

        parser.parse(filePath);

        // Vérifications "structurelles" sans connaître le contenu exact du fichier
        assertTrue(scene.getWidth() > 0, "La largeur doit être > 0");
        assertTrue(scene.getHeight() > 0, "La hauteur doit être > 0");

        assertNotNull(scene.getCamera(), "La caméra doit être définie");
        assertNotNull(scene.getCamera().getPosition());
        assertNotNull(scene.getCamera().getLookAt());

        assertNotNull(scene.getShapes(), "La liste de formes ne doit pas être null");
        assertFalse(scene.getShapes().isEmpty(), "La scène devrait contenir au moins une forme");
    }

    // Test complet sur une scène simple générée à la volée :
    @Test
    public void testParseSimpleSphereScene() throws IOException {
        Scene scene = new Scene();
        SceneFileParser parser = new SceneFileParser(scene);

        String content = """
            # Scène simple pour tester le parser

            size 800 600
            output test.png

            camera 0 0 -5   0 0 0   0 1 0   45

            ambient 0.1 0.1 0.1
            diffuse 0.5 0.0 0.0
            specular 0.2 0.2 0.2

            sphere 0 0 0 1
        """;

        Path temp = Files.createTempFile("scene_simple_sphere", ".test");
        Files.writeString(temp, content);

        parser.parse(temp.toString());

        // Vérifs de base
        assertEquals(800, scene.getWidth());
        assertEquals(600, scene.getHeight());

        assertNotNull(scene.getCamera());
        assertEquals(new Point(0, 0, -5), scene.getCamera().getPosition());
        assertEquals(new Point(0, 0, 0), scene.getCamera().getLookAt());
        assertEquals(new Vector(0, 1, 0), scene.getCamera().getUp());

        // Ambient
        Color ambient = scene.getAmbient();
        assertNotNull(ambient);
        assertEquals(new Color(0.1f, 0.1f, 0.1f), ambient);

        // Une seule forme : une sphere
        assertEquals(1, scene.getShapes().size());
        assertTrue(scene.getShapes().get(0) instanceof Sphere);
        Sphere s = (Sphere) scene.getShapes().get(0);

        assertEquals(new Point(0, 0, 0), s.getCenter());
        assertEquals(1.0, s.getRadius(), 1e-9);
        assertEquals(new Color(0.5f, 0.0f, 0.0f), s.getDiffuse());
        assertEquals(new Color(0.2f, 0.2f, 0.2f), s.getSpecular());
    }

    // Test : ambient + diffuse > 1 sur une composante doit lever IllegalArgumentException
    @Test
    public void testAmbientPlusDiffuseGreaterThanOneThrows() throws IOException {
        Scene scene = new Scene();
        SceneFileParser parser = new SceneFileParser(scene);

        String content = """
            size 100 100
            camera 0 0 1   0 0 0   0 1 0   45

            ambient 0.8 0.2 0.0
            diffuse 0.5 0.0 0.0

            sphere 0 0 0 1
        """;

        Path temp = Files.createTempFile("scene_ambient_diffuse", ".test");
        Files.writeString(temp, content);

        assertThrows(IllegalArgumentException.class,
                () -> parser.parse(temp.toString()),
                "ambient + diffuse > 1 sur une composante devrait lever IllegalArgumentException");
    }

    /**
     * Test : tri avec un index hors limites doit lever IndexOutOfBoundsException.
     */
    @Test
    public void testTriWithInvalidIndexThrows() throws IOException {
        Scene scene = new Scene();
        SceneFileParser parser = new SceneFileParser(scene);

        String content = """
            size 200 200
            camera 0 0 1   0 0 0   0 1 0   45

            maxverts 2
            vertex 0 0 0
            vertex 1 0 0

            # Ici on utilise un index 2 alors que maxverts = 2 → indices valides : 0, 1
            tri 0 1 2
        """;

        Path temp = Files.createTempFile("scene_tri_invalid", ".test");
        Files.writeString(temp, content);

        assertThrows(IndexOutOfBoundsException.class,
                () -> parser.parse(temp.toString()),
                "Un tri avec un index >= vertexCount doit lever IndexOutOfBoundsException");
    }
}

