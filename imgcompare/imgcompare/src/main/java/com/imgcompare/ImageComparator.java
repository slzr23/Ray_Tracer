package com.imgcompare;


import java.awt.image.BufferedImage;

public class ImageComparator {
    private final BufferedImage img1;
    private final BufferedImage img2;

    public ImageComparator(BufferedImage img1, BufferedImage img2) {
        this.img1 = img1;
        this.img2 = img2;
    }

    public int countDifferentPixels() {
        int width = Math.min(img1.getWidth(), img2.getWidth());
        int height = Math.min(img1.getHeight(), img2.getHeight());
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

    public BufferedImage generateDifferenceImage() {
        int width = Math.max(img1.getWidth(), img2.getWidth());
        int height = Math.max(img1.getHeight(), img2.getHeight());
        BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = (x < img1.getWidth() && y < img1.getHeight()) ? img1.getRGB(x, y) : 0;
                int rgb2 = (x < img2.getWidth() && y < img2.getHeight()) ? img2.getRGB(x, y) : 0;

                // DEBUG :ystem.out.println(rgb1 + ";" + rgb2);

                if (rgb1 != rgb2) {
                    // différence pour chaque composante RGB
                    int r1 = (rgb1 >> 16) & 0xFF;
                    int g1 = (rgb1 >> 8) & 0xFF;
                    int b1 = rgb1 & 0xFF;
                    
                    int r2 = (rgb2 >> 16) & 0xFF;
                    int g2 = (rgb2 >> 8) & 0xFF;
                    int b2 = rgb2 & 0xFF;
                    
                    int diffR = Math.abs(r1 - r2);
                    int diffG = Math.abs(g1 - g2);
                    int diffB = Math.abs(b1 - b2);
                    
                    // Assemblage couleur différence en RGB
                    int diffRGB = (diffR << 16) | (diffG << 8) | diffB;
                    diffImage.setRGB(x, y, diffRGB);
                } else {
                    diffImage.setRGB(x, y, 0x000000); // Noir pour les pixels identiques
                }
            }
        }
        return diffImage;
    }


}