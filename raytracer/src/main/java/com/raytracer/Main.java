package com.raytracer;

import com.imaging.Color;
import com.parsing.SceneFileParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java com.raytracer.Main <scene-file>");
            System.exit(1);
        }

        String sceneFilePath = args[0];
        
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
            
            // Étape 4 : Boucle de rendu - pour chaque pixel
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    // Calculer la couleur du pixel
                    Color color = rayTracer.getPixelColor(i, j);
                    
                    // Convertir la couleur en RGB et l'assigner au pixel
                    int rgb = colorToRGB(color);
                    image.setRGB(i, j, rgb);
                }
                
                // Afficher la progression
                if ((j + 1) % 50 == 0 || j == height - 1) {
                    System.out.println("  Progression : " + (j + 1) + "/" + height + " lignes");
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
    
    /**
     * Convertit une Color (valeurs float 0-1) en int RGB pour BufferedImage
     */
    private static int colorToRGB(Color color) {
        // Clamp les valeurs entre 0 et 1
        float r = Math.max(0f, Math.min(1f, color.getR()));
        float g = Math.max(0f, Math.min(1f, color.getG()));
        float b = Math.max(0f, Math.min(1f, color.getB()));
        
        // Convertir en 0-255
        int red = (int) (r * 255);
        int green = (int) (g * 255);
        int blue = (int) (b * 255);
        
        // Combiner en un seul int RGB (format : 0xRRGGBB)
        return (red << 16) | (green << 8) | blue;
    }
}
