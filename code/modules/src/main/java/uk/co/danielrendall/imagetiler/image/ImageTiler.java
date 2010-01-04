package uk.co.danielrendall.imagetiler.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 20, 2006
 * Time: 1:38:51 PM
 */
public class ImageTiler {

    public final static String INPUT_FILENAME = "C:\\heart.tif";
    public final static String OUTPUT_FILENAME = "C:\\heartout.tif";

    public static void main(String[] args) {
        ImageTile imageTile = new GemImageTile();
        imageTile.initialize(args);
        final int tileWidth = imageTile.getWidth();
        final int tileHeight = imageTile.getHeight();
        try {
            File inFile = new File(INPUT_FILENAME);
            BufferedImage bi = ImageIO.read(inFile);

            BufferedImage biOut = new BufferedImage(bi.getWidth() * tileWidth, bi.getHeight() * tileHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g =biOut.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0,0,biOut.getWidth(), biOut.getHeight());

            WritableRaster wr = biOut.getRaster();


            for (int y=0; y < bi.getHeight(); y++) {
                for (int x=0; x < bi.getWidth(); x++) {
                    int pixel[] = new int[4];
                    bi.getRaster().getPixel(x,y,pixel);
                    Color color = new Color(pixel[0], pixel[1], pixel[2]);
                    int xTile = x * tileWidth;
                    int yTile = y * tileHeight;
                    imageTile.getTile(biOut.getSubimage(xTile, yTile, tileWidth, tileHeight), color);

                }
            }
            File outFile = new File(OUTPUT_FILENAME);
            biOut.setData(wr);
            ImageIO.write(biOut, "tif", outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
