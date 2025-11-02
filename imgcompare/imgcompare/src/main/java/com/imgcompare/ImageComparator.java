package com.imgcompare;


import java.awt.image.BufferedImage;

/**
 * Classe responsable de la comparaison de deux images.
 * Elle permet de compter les pixels différents et de générer une image différentielle.
 */
public class ImageComparator {

    private final BufferedImage img1;
    private final BufferedImage img2;

    /**
     * Constructeur de la classe ImageComparator.
     * @param img1 première image à comparer
     * @param img2 seconde image à comparer
     */
    public ImageComparator(BufferedImage img1, BufferedImage img2) {
        this.img1 = img1;
        this.img2 = img2;
    }

    /**
     * Compte le nombre de pixels différents entre les deux images.
     * @return le nombre de pixels différents
     * @throws IllegalArgumentException si les images n'ont pas la même taille
     */
    public int countDifferentPixels() {
        int width = img1.getWidth();
        int height = img1.getHeight();

        if (width != img2.getWidth() || height != img2.getHeight()) {
            throw new IllegalArgumentException("Les images doivent avoir la même taille.");
        }

        int diffCount = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    diffCount++;
                }
            }
        }

        return diffCount;
    }

    /**
     * Génère une image différentielle où :
     * - les pixels identiques sont noirs (0x000000)
     * - les pixels différents représentent la différence de couleur entre les deux images.
     * @return une nouvelle BufferedImage contenant l'image différentielle
     * @throws IllegalArgumentException si les images n'ont pas la même taille
     */
    public BufferedImage generateDifferenceImage() {
        int width = img1.getWidth();
        int height = img1.getHeight();

        if (width != img2.getWidth() || height != img2.getHeight()) {
            throw new IllegalArgumentException("Les images doivent avoir la même taille.");
        }

        BufferedImage diff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);

                if (rgb1 == rgb2) {
                    diff.setRGB(x, y, 0x000000); // noir si identique
                } else {
                    int r1 = (rgb1 >> 16) & 0xFF;
                    int g1 = (rgb1 >> 8) & 0xFF;
                    int b1 = rgb1 & 0xFF;

                    int r2 = (rgb2 >> 16) & 0xFF;
                    int g2 = (rgb2 >> 8) & 0xFF;
                    int b2 = rgb2 & 0xFF;

                    int dr = Math.abs(r1 - r2);
                    int dg = Math.abs(g1 - g2);
                    int db = Math.abs(b1 - b2);

                    int diffColor = (dr << 16) | (dg << 8) | db;
                    diff.setRGB(x, y, diffColor);
                }
            }
        }

        return diff;
    }
}
