package com.imgcompare;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar \"imgcompare\\imgcompare\\target\\imgcompare.jar\" \"<image1>\" \"<image2>\"");
            return;
        }

        try {
            BufferedImage img1 = ImageIO.read(new File(args[0]));
            BufferedImage img2 = ImageIO.read(new File(args[1]));

            ImageComparator comparator = new ImageComparator(img1, img2);

            int diffCount = comparator.countDifferentPixels();

            if (diffCount < 1000) {
                String result = "OK";
                System.out.println(result);
            } else {
                String result = "KO";
                System.out.println(result);
            }
            System.out.println("Les deux images diffèrent de " + diffCount + " pixels.");

            // Générer l'image différentielle seulement si au moins un pixel diffère
            if (diffCount > 0) {
                BufferedImage diffImage = comparator.generateDifferenceImage();
                ImageIO.write(diffImage, "png", new File("diff.png"));
                System.out.println("Image différentielle générée : diff.png");
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture ou l’écriture des fichiers : " + e.getMessage());
        }
    }
}
