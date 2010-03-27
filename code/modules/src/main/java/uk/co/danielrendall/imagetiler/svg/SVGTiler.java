package uk.co.danielrendall.imagetiler.svg;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.ScannerStrategy;
import uk.co.danielrendall.imagetiler.ScannerStrategyFactory;

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

    private final File inputFile;
    private final File outputFile;
    private final String type;
    private final String strategy;
    private Document document;

    private static double SCALE = 10.0;

    public SVGTiler(File inputFile, File outputFile, String type, String strategy) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.type = type;
        this.strategy = strategy;
    }


    public void process() {
        try {
            SVGTile svgTile = (SVGTile) Class.forName("uk.co.danielrendall.imagetiler.svg.tiles." + type + "SVGTile").newInstance();
            ScannerStrategyFactory factory = new ScannerStrategyFactory(strategy);
    
//            svgTile.initialize(args);

            SVGDOMImplementation domImpl = (SVGDOMImplementation) SVGDOMImplementation.getDOMImplementation();
            document = domImpl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

            try {
                BufferedImage input = ImageIO.read(inputFile);
                if (input != null) {

                    int width = input.getWidth();
                    int height = input.getHeight();

                    double svgWidth = ((double)width) * SCALE;
                    double svgHeight = ((double)height) * SCALE;


                    Element root = document.getDocumentElement();
                    root.setAttributeNS(null, "width", "" + svgWidth);
                    root.setAttributeNS(null, "height", "" + svgHeight);

                    Element outerGroup = createElement("g");

                    final Raster raster = input.getRaster();
                    log.debug("There are " + raster.getNumBands() + " bands");
                    ScannerStrategy scannerStrategy = factory.createStrategy(0, width, 0, height);
                    while (scannerStrategy.hasNext()) {
                        Pixel p = scannerStrategy.next();
                        int x = p.getX();
                        int y = p.getY();
                        int pixel[] = new int[4];
                        log.debug("Getting x=" + x + " y=" + y);
                        raster.getPixel(x,y,pixel);
                        Color color = new Color(pixel[0], pixel[1], pixel[2]);

                        double left = SCALE * ((double) x  - (width/(double)2.0));
                        double top = SCALE * ((double) y  - (height/(double)2.0));
                        double right = left + SCALE;
                        double bottom = top + SCALE;

                        Element group = createElement("g");
                        //group.setAttributeNS(null, "transform","translate(100,100)");
                        if (svgTile.getTile(group, new TileContext(left, right, top, bottom, color, this))) {
                            outerGroup.appendChild(group);
                        } else {
                            log.debug("Skipping tile at x=" + x + " y=" + y);
                        }

                    }
                    outerGroup.setAttributeNS(null, "transform","translate(" + (svgWidth/(double)2.0) + "," + (svgHeight/(double)2.0) + ")");
                    root.appendChild(outerGroup);

                    SVGTranscoder t = new SVGTranscoder();
                    TranscoderInput transInput = new TranscoderInput(document);
                    Writer writer = new FileWriter(outputFile);
                    TranscoderOutput transOutput = new TranscoderOutput(writer);
                    t.transcode(transInput, transOutput);
                    writer.flush();
                    writer.close();
                } else {
                    log.debug("Couldn't read image");
                }
            } catch (IOException e) {
                 e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (TranscoderException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Element createElement(String name) {
        return document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, name);
    }

    public int getScale() {
        return (int)SCALE;
    }
}
