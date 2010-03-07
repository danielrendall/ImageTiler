package uk.co.danielrendall.imagetiler;

import uk.co.danielrendall.imagetiler.image.*;
import uk.co.danielrendall.imagetiler.svg.*;
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
public class App {

    @Option(name = "-i", usage = "the input file", required = true)
    private File inputFile;

    @Option(name = "-o", usage = "the output file", required = true)
    private File outputFile;

    @Option(name = "-f", usage = "output format (svg or image)", required = true)
    private String outputFormat = "image";

    @Option(name = "-t", usage = "tile type", required = false)
    private String type = "Simple";

    public static void main(String[] args) {
        new App().doMain(args);
    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        parser.setUsageWidth(80);

        try {
            // parse the arguments.
            parser.parseArgument(args);
            if ("image".equalsIgnoreCase(outputFormat)) {
                new ImageTiler(inputFile, outputFile, type).process();
            } else {
                new SVGTiler(inputFile, outputFile, type).process();
            }

        } catch (CmdLineException e) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("java App [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();
        }
    }

}
