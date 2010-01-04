package uk.co.danielrendall.imagetiler.svg;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

    public final static String INPUT_FILENAME = "/home/daniel/Desktop/smiley.png";
    public final static String OUTPUT_FILENAME = "/home/daniel/Desktop/smiley.svg";

    private static double SCALE = 10.0;
    private final SVGDOMImplementation domImpl;
    private final String svgNS;
    private final Document document;

    public static void main(String[] args) {

        SVGTiler tiler = new SVGTiler(args);

    }

    private SVGTiler(String[] args) {
        SVGTile svgTile = new SimpleSVGTile();
        svgTile.initialize(args);

        domImpl = (SVGDOMImplementation) SVGDOMImplementation.getDOMImplementation();
        svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        document = domImpl.createDocument(svgNS, "svg", null);

        try {
            File inFile = new File(INPUT_FILENAME);
            BufferedImage input = ImageIO.read(inFile);
            if (input == null) {
                System.err.println("Input was null!");
            }

            int width = input.getWidth();
            int height = input.getHeight();

            double svgWidth = ((double)width) * SCALE;
            double svgHeight = ((double)height) * SCALE;


            Element root = document.getDocumentElement();
            root.setAttributeNS(null, "width", "" + svgWidth);
            root.setAttributeNS(null, "height", "" + svgHeight);

            Element outerGroup = createElement("g");

            final Raster raster = input.getRaster();
            System.out.println("There are " + raster.getNumBands() + " bands");
            for (int y=0; y < height; y++) {
                for (int x=0; x < width; x++) {
                    int pixel[] = new int[4];
                    System.out.println("Getting x=" + x + " y=" + y);
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
                        System.err.println("Skipping tile at x=" + x + " y=" + y);
                    }

                }
            }
            outerGroup.setAttributeNS(null, "transform","translate(" + (svgWidth/(double)2.0) + "," + (svgHeight/(double)2.0) + ")");
            root.appendChild(outerGroup);

            SVGTranscoder t = new SVGTranscoder();
            TranscoderInput transInput = new TranscoderInput(document);
            Writer writer = new FileWriter(OUTPUT_FILENAME);
            TranscoderOutput transOutput = new TranscoderOutput(writer);
            t.transcode(transInput, transOutput);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public Element createElement(String name) {
        return document.createElementNS(svgNS, name);
    }

    public int getScale() {
        return (int)SCALE;
    }
}
