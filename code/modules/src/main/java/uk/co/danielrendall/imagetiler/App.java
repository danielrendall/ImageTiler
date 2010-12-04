package uk.co.danielrendall.imagetiler;

import uk.co.danielrendall.imagetiler.image.ImageTiler;
import uk.co.danielrendall.imagetiler.shared.ConfigStore;
import uk.co.danielrendall.imagetiler.svg.*;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

import java.io.File;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 20, 2006
 * Time: 1:38:51 PM
 */
public class App {

    @Option(name = "-i", usage = "the input file", required = false)
    private File inputFile = null;

    @Option(name = "-o", usage = "the output file", required = false)
    private File outputFile = null;

    @Option(name = "-f", usage = "output format (svg or image)", required = false)
    private String outputFormat = "svg";

    @Option(name = "-t", usage = "tile type", required = false)
    private String type = "Simple";

    @Option(name = "-s", usage = "scan strategy", required = false)
    private String strategy = "Grid";

    @Option(name = "-c", usage = "configuration", required = false)
    private String config = "";

    @Option(name = "-h", usage = "help", required = false)
    private boolean help = false;

    public static void main(String[] args) {
        new App().doMain(args);
    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        parser.setUsageWidth(80);

        try {
            // parse the arguments.
            parser.parseArgument(args);
            ConfigStore store = new ConfigStore(config);
            if ("image".equalsIgnoreCase(outputFormat)) {
                ImageTiler tiler = new ImageTiler(type, strategy, store);
                if (help) {
                    System.out.println("No help available");
                } else {
                    if (inputFile == null || outputFile == null) {
                        throw new CmdLineException("Input and output files must be supplied");
                    }
                    tiler.process(inputFile, outputFile);
                }
            } else {
                SVGTiler tiler = new SVGTiler(type, strategy, store);
                if (help) {
                    System.out.println("For tile type '" + type + "'");
                    System.out.println(tiler.describeOptions());
                } else {
                    if (inputFile == null || outputFile == null) {
                        throw new CmdLineException("Input and output files must be supplied");
                    }
                    tiler.process(inputFile, outputFile);
                }
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
