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

package uk.co.danielrendall.imagetiler.svg;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.shared.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 9:43:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class SVGTiler {

    public final static Logger log = Logger.getLogger(SVGTiler.class);

    private final SVGTileFactory tileFactory;
    private final ScannerStrategyFactory strategyFactory;

    // reasonable hard-coded value, could be made configurable, but user
    // could scale the resulting image manually anyway.
    private static double SCALE = 10.0;

    public SVGTiler(String type, String strategy, ConfigStore store) {
        tileFactory = new SVGTileFactory(type, store);
        strategyFactory = new ScannerStrategyFactory(strategy);
    }

    public void process(File inputFile, File outputFile) {
        try {
            BufferedImage input = ImageIO.read(inputFile);
            if (input != null) {
                Document document = process(input);
                if (document != null) {
                    SVGTranscoder t = new SVGTranscoder();
                    TranscoderInput transInput = new TranscoderInput(document);
                    Writer writer = new FileWriter(outputFile);
                    TranscoderOutput transOutput = new TranscoderOutput(writer);
                    t.transcode(transInput, transOutput);
                    writer.flush();
                    writer.close();
                }
            } else {
                log.debug("Couldn't read image");
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Document process(BufferedImage input) {
        try {

            // Can't simply create a ScannerStrategy now, because we need to configure it with the
            // image being processed so it can do intelligent things with white pixels.

            // Old implementation - required the tile to do sensible things with
            // the command-line arguments. Better way - use the ConfigStore to
            // manage conversion to doubles etc.
            // Another way - have a TileFactory which does the instantiation of the
            // tile and preconfigures 'global' command-line settings for it, because
            // currently each time getTile() is called it will be pulling the same
            // data from the ConfigStore. But it's not as if speed / efficiency are
            // pressing issues.
//            svgTile.initialize(args);

            SVGDOMImplementation domImpl = (SVGDOMImplementation) SVGDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

            SVGTile svgTile = tileFactory.getTile();

                int width = input.getWidth();
                int height = input.getHeight();

                double svgWidth = ((double) width) * SCALE;
                double svgHeight = ((double) height) * SCALE;


                Element root = document.getDocumentElement();
                root.setAttributeNS(null, "width", "" + svgWidth);
                root.setAttributeNS(null, "height", "" + svgHeight);

                Element outerGroup = createElement(document, "g");

                final Raster raster = input.getRaster();
                log.debug("There are " + raster.getNumBands() + " bands");
                ScannerStrategy scannerStrategy = strategyFactory.createStrategy(0, width, 0, height, new RasterPixelFilter(raster));
                while (scannerStrategy.hasNext()) {
                    Pixel p = scannerStrategy.next();
                    int x = p.getX();
                    int y = p.getY();
                    int pixel[] = new int[4];
                    log.debug("Getting x=" + x + " y=" + y);
                    raster.getPixel(x, y, pixel);
                    Color color = new Color(pixel[0], pixel[1], pixel[2]);

                    double left = SCALE * ((double) x - (width / (double) 2.0));
                    double top = SCALE * ((double) y - (height / (double) 2.0));
                    double right = left + SCALE;
                    double bottom = top + SCALE;

                    Element group = createElement(document, "g");
                    //group.setAttributeNS(null, "transform","translate(100,100)");
                    if (svgTile.getTile(group, new TileContext(left, right, top, bottom, color, document, this))) {
                        outerGroup.appendChild(group);
                    } else {
                        log.debug("Skipping tile at x=" + x + " y=" + y);
                    }

                }
                outerGroup.setAttributeNS(null, "transform", "translate(" + (svgWidth / (double) 2.0) + "," + (svgHeight / (double) 2.0) + ")");
                root.appendChild(outerGroup);

                return document;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Element createElement(Document document, String name) {
        return document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, name);
    }

    public int getScale() {
        return (int) SCALE;
    }

    public String describeOptions() {
        return tileFactory.describeOptions();
    }
}
