package uk.co.danielrendall.imagetiler.image;

import uk.co.danielrendall.imagetiler.RasterPixelFilter;
import uk.co.danielrendall.imagetiler.ScannerStrategy;
import uk.co.danielrendall.imagetiler.ScannerStrategyFactory;
import uk.co.danielrendall.imagetiler.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 20, 2006
 * Time: 1:38:51 PM
 */
public class ImageTiler {

    private final File inputFile;
    private final File outputFile;
    private final String type;
    private final String strategy;

    public ImageTiler(File inputFile, File outputFile, String type, String strategy) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.type = type;
        this.strategy = strategy;
    }


    public void process() {
        try {
            ImageTile imageTile = (ImageTile) Class.forName("uk.co.danielrendall.imagetiler.image.tiles." + type + "ImageTile").newInstance();
            ScannerStrategyFactory factory = new ScannerStrategyFactory(strategy);

            final int tileWidth = imageTile.getWidth();
            final int tileHeight = imageTile.getHeight();
            try {
                BufferedImage bi = ImageIO.read(inputFile);

                if (bi != null) {

                    BufferedImage biOut = new BufferedImage(bi.getWidth() * tileWidth, bi.getHeight() * tileHeight, BufferedImage.TYPE_INT_RGB);

                    Graphics2D g =biOut.createGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0,0,biOut.getWidth(), biOut.getHeight());

                    final Raster raster = bi.getRaster();
                    WritableRaster wr = biOut.getRaster();

                    ScannerStrategy scannerStrategy = factory.createStrategy(0, bi.getWidth(), 0, bi.getHeight(), new RasterPixelFilter(raster));
                    for (Pixel p = scannerStrategy.next(); scannerStrategy.hasNext(); p = scannerStrategy.next()) {
                        int x = p.getX();
                        int y = p.getY();
                        int pixel[] = new int[4];
                        raster.getPixel(x,y,pixel);
                        Color color = new Color(pixel[0], pixel[1], pixel[2]);
                        int xTile = x * tileWidth;
                        int yTile = y * tileHeight;
                        imageTile.getTile(biOut.getSubimage(xTile, yTile, tileWidth, tileHeight), color);

                    }
                    biOut.setData(wr);
                    ImageIO.write(biOut, "jpg", outputFile);
                } else {
                    System.out.println("Couldn't read image");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
