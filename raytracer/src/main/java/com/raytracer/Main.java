package com.raytracer;

import com.imaging.Color;
import com.parsing.SceneFileParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Point d'entrée du ray tracer.
 * <p>
 * Charge un fichier scène passé en argument, effectue le rendu
 * en parallèle (multi-threadé par lignes) et sauvegarde l'image PNG.
 * </p>
 * <p>
 * Usage : {@code java com.raytracer.Main <fichier.scene>}
 * </p>
 * 
 * @author Projet Ray Tracer
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java com.raytracer.Main <scene-file>");
            System.exit(1);
        }

        String sceneFilePath = "src/main/resources/scenes/" + args[0]; // jalonX/fichier.test
        
        try {
            // Étape 1 : Charger la scène depuis le fichier
            System.out.println("Chargement de la scène depuis : " + sceneFilePath);
            Scene scene = new Scene();
            SceneFileParser parser = new SceneFileParser(scene);
            parser.parse(sceneFilePath);
            
            // Étape 2 : Créer le RayTracer
            RayTracer rayTracer = new RayTracer(scene);
            
            // Étape 3 : Créer l'image (BufferedImage)
            int width = scene.getWidth();
            int height = scene.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            System.out.println("Rendu de l'image (" + width + "x" + height + ")...");

            // Rendu multi-threadé par lignes, stockage temporaire des pixels
            int[][] pixelBuffer = new int[height][width];
            int threadCount = Math.max(1, Runtime.getRuntime().availableProcessors());
            ExecutorService pool = Executors.newFixedThreadPool(threadCount);
            List<Callable<Void>> jobs = new ArrayList<>();
            AtomicInteger rowsDone = new AtomicInteger(0);

            for (int j = 0; j < height; j++) {
                final int row = j;
                jobs.add(() -> {
                    for (int i = 0; i < width; i++) {
                        Color color = rayTracer.getPixelColor(i, row);
                        pixelBuffer[row][i] = color.toRGB();
                    }
                    int finished = rowsDone.incrementAndGet();
                    if (finished % 50 == 0 || finished == height) {
                        System.out.println("  Progression : " + finished + "/" + height + " lignes");
                    }
                    return null;
                });
            }

            try {
                pool.invokeAll(jobs);
                pool.shutdown();
                pool.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Rendu interrompu", e);
            }

            // Copier les pixels calculés dans l'image
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    image.setRGB(i, j, pixelBuffer[j][i]);
                }
            }
            
            // Étape 5 : Sauvegarder l'image en PNG
            String outputPath = scene.getOutputFile();
            File outputFile = new File(outputPath);
            
            // Créer les répertoires parents si nécessaire
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            ImageIO.write(image, "PNG", outputFile);
            System.out.println("Image sauvegardée : " + outputPath);
            
        } catch (IOException e) {
            System.err.println("Erreur lors du traitement du fichier : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
