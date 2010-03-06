package uk.co.danielrendall.imagetiler.image;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

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

    @Option(name = "-i", usage = "the input file", required = true)
    private File inputFile;

    @Option(name = "-o", usage = "the output file", required = true)
    private File outputFile;

    @Option(name = "-t", usage = "tile type", required = false)
    private String type = "Simple";

    public static void main(String[] args) {
        new ImageTiler().doMain(args);
    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        parser.setUsageWidth(80);

        try {
            // parse the arguments.
            parser.parseArgument(args);
            process();

        } catch (CmdLineException e) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("java ImageTiler [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();
        }
    }

    private void process() {
        try {
        ImageTile imageTile = (ImageTile) Class.forName("uk.co.danielrendall.imagetiler.image." + type + "ImageTile").newInstance();
//        imageTile.initialize(args);
        final int tileWidth = imageTile.getWidth();
        final int tileHeight = imageTile.getHeight();
        try {
            BufferedImage bi = ImageIO.read(inputFile);

            if (bi != null) {

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
