package com.imgcompare;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java imgcompare.Main <image1.png> <image2.png>");
            return;
        }

        try {
            BufferedImage img1 = ImageIO.read(new File(args[0]));
            BufferedImage img2 = ImageIO.read(new File(args[1]));

            ImageComparator comparator = new ImageComparator(img1, img2);

            int diffCount = comparator.countDifferentPixels();
            BufferedImage diffImage = comparator.generateDifferenceImage();

            String result = diffCount < 1000 ? "OK" : "KO";
            System.out.println(result);
            System.out.println("Les deux images diffèrent de " + diffCount + " pixels.");

            ImageIO.write(diffImage, "png", new File("diff.png"));

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture ou l’écriture des fichiers : " + e.getMessage());
        }
    }
}
