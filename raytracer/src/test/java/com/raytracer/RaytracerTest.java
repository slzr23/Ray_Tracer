package com.raytracer;

import com.imgcompare.ImageComparator;
import com.parsing.SceneFileParser;
import com.imaging.Color;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class RaytracerTest {

    private static class JalonCase {
        final String name;
        final String jalonDir;   // ex: "jalon3"
        final String sceneFile;  // ex: "tp31.test"
        final String refPng;     // ex: "tp31.png"

        JalonCase(String name, String jalonDir, String sceneFile, String refPng) {
            this.name = name;
            this.jalonDir = jalonDir;
            this.sceneFile = sceneFile;
            this.refPng = refPng;
        }
    }

    // jalon3 : ok
    @Test
    void testJalon3Images() throws Exception {
        JalonCase[] cases = new JalonCase[]{
                new JalonCase("tp31", "jalon3", "tp31.test", "tp31.png"),
                new JalonCase("tp32", "jalon3", "tp32.test", "tp32.png"),
                new JalonCase("tp33", "jalon3", "tp33.test", "tp33.png"),
                new JalonCase("tp34", "jalon3", "tp34.test", "tp34.png"),
                new JalonCase("tp35", "jalon3", "tp35.test", "tp35.png")
        };
        runJalonCases(cases);
    }
    // jalon 4 : ne passe pas à cause de ce quia été demandé en jalon 5
    @Test
    void testJalon4Images() throws Exception {
        JalonCase[] cases = new JalonCase[]{
                new JalonCase("tp41-dir", "jalon4", "tp41-dir.test", "tp41-dir.png"),
                new JalonCase("tp41-point", "jalon4", "tp41-point.test", "tp41-point.png"),
                new JalonCase("tp42-dir", "jalon4", "tp42-dir.test", "tp42-dir.png"),
                new JalonCase("tp42-point", "jalon4", "tp42-point.test", "tp42-point.png"),
                new JalonCase("tp43", "jalon4", "tp43.test", "tp43.png"),
                new JalonCase("tp44", "jalon4", "tp44.test", "tp44.png"),
                new JalonCase("tp45", "jalon4", "tp45.test", "tp45.png")
        };
        runJalonCases(cases);
    }

    // jalon 5 : problème sur tp53 (il manque le triangle en profondeur)
    @Test
    void testJalon5Images() throws Exception {
        JalonCase[] cases = new JalonCase[]{
                new JalonCase("tp51-diffuse", "jalon5", "tp51-diffuse.test", "tp51-diffuse.png"),
                new JalonCase("tp51-specular", "jalon5", "tp51-specular.test", "tp51-specular.png"),
                new JalonCase("tp52", "jalon5", "tp52.test", "tp52.png"),
                new JalonCase("tp53", "jalon5", "tp53.test", "tp53.png"),
                new JalonCase("tp54", "jalon5", "tp54.test", "tp54.png"),
                new JalonCase("tp55", "jalon5", "tp55.test", "tp55.png")
        };
        runJalonCases(cases);
    }

    // jalon6 : plusieurs problèmes sur certaines images
    @Test
    void testJalon6Images() throws Exception {
        JalonCase[] cases = new JalonCase[]{
                new JalonCase("tp61", "jalon6", "tp61.test", "tp61.png"),
                new JalonCase("tp61-dir", "jalon6", "tp61-dir.test", "tp61-dir.png"),
                new JalonCase("tp62-1", "jalon6", "tp62-1.test", "tp62-1.png"),
                new JalonCase("tp62-2", "jalon6", "tp62-2.test", "tp62-2.png"),
                new JalonCase("tp62-3", "jalon6", "tp62-3.test", "tp62-3.png"),
                new JalonCase("tp62-4", "jalon6", "tp62-4.test", "tp62-4.png"),
                new JalonCase("tp62-5", "jalon6", "tp62-5.test", "tp62-5.png"),
                new JalonCase("tp63", "jalon6", "tp63.test", "tp63.png"),
                new JalonCase("tp64", "jalon6", "tp64.test", "tp64.png")
        };
        runJalonCases(cases);
    }

    private void runJalonCases(JalonCase[] cases) throws Exception {
        for (JalonCase c : cases) {
            Path scenePath = Paths.get("src", "main", "resources", "scenes", c.jalonDir, c.sceneFile);
            Path refImagePath = Paths.get("src", "main", "resources", "scenes", c.jalonDir, c.refPng);

            assertTrue(Files.exists(scenePath),
                    () -> "Fichier de scène manquant pour " + c.name + " : " + scenePath);
            assertTrue(Files.exists(refImagePath),
                    () -> "Image de référence manquante pour " + c.name + " : " + refImagePath);

            BufferedImage rendered = renderScene(scenePath);
            BufferedImage reference = loadImage(refImagePath);

            assertImagesSimilar(c.name, rendered, reference);
        }
    }

    private BufferedImage renderScene(Path sceneFile) throws IOException {
        Scene scene = new Scene();
        SceneFileParser parser = new SceneFileParser(scene);
        parser.parse(sceneFile.toString());

        RayTracer rayTracer = new RayTracer(scene);
        int width = scene.getWidth();
        int height = scene.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = rayTracer.getPixelColor(x, y);
                img.setRGB(x, y, color.toRGB());
            }
        }
        return img;
    }

    private BufferedImage loadImage(Path path) throws IOException {
        return ImageIO.read(path.toFile());
    }

    private void assertImagesSimilar(String name, BufferedImage img1, BufferedImage img2) {
        ImageComparator comparator = new ImageComparator(img1, img2);
        int diffCount = comparator.countDifferentPixels();

        int threshold = 1000; // même logique que imgcompare
        assertTrue(diffCount < threshold,
                () -> "Les images pour " + name + " diffèrent sur " + diffCount +
                        " pixels (seuil autorisé = " + threshold + ")");
    }
}
