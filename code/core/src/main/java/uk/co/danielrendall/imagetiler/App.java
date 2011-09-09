/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of ImageTiler.
 *
 * ImageTiler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ImageTiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ImageTiler.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.imagetiler;

import uk.co.danielrendall.imagetiler.registry.PluginRegistry;
import uk.co.danielrendall.imagetiler.shared.ConfigStore;
import uk.co.danielrendall.imagetiler.shared.FileConfigStore;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
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
            PluginRegistry pluginRegistry = ImageTilerApplication.createPluginRegistry();
            // parse the arguments.
            parser.parseArgument(args);
            ConfigStore store = new FileConfigStore(config);
            if (help) {
                System.out.println("For tile type '" + type + "'");
//                System.out.println(tiler.describeOptions());
            } else {
                if (inputFile == null || outputFile == null) {
                    throw new CmdLineException("Input and output files must be supplied");
                }
                SVGTile tile = (SVGTile) pluginRegistry.getConfiguredInstance(ImageTilerApplication.PLUGIN_TYPE_TILE, type, store);
                ScannerStrategy scannerStrategy = (ScannerStrategy) pluginRegistry.getConfiguredInstance(ImageTilerApplication.PLUGIN_TYPE_STRATEGY, strategy, store);
                SVGTiler tiler = new SVGTiler(tile, scannerStrategy);
                if (inputFile == null || outputFile == null) {
                    throw new CmdLineException("Input and output files must be supplied");
                }
                tiler.process(inputFile, outputFile);
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
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
