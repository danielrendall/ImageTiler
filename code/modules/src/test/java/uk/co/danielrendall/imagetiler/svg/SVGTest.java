/*
 * Copyright (c) 2009, 2010 Daniel Rendall
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
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.junit.Ignore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileWriter;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Jul 31, 2006
 * Time: 10:46:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
public class SVGTest {

    public Document createDocument() {
        SVGDOMImplementation impl = (SVGDOMImplementation) SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document document =
                impl.createDocument(svgNS, "svg", null);
        Element root = document.getDocumentElement();
        root.setAttributeNS(null, "width", "450");
        root.setAttributeNS(null, "height", "500");


        Element group;

        group = document.createElementNS(svgNS, "g");
        //group.setAttributeNS(null, "transform","translate(100,100)");
        group.setAttributeNS(null, "transform","rotate(15)");
        root.appendChild(group);

        Element e;
        e = document.createElementNS(svgNS, "rect");
        e.setAttributeNS(null, "x", "10");
        e.setAttributeNS(null, "y", "10");
        e.setAttributeNS(null, "width", "200");
        e.setAttributeNS(null, "height", "300");
        e.setAttributeNS(null, "style", "fill:red;stroke:black;stroke-width:4");
        group.appendChild(e);

        e = document.createElementNS(svgNS, "circle");
        e.setAttributeNS(null, "cx", "225");
        e.setAttributeNS(null, "cy", "250");
        e.setAttributeNS(null, "r", "100");
        e.setAttributeNS(null, "style", "fill:green;fill-opacity:.5");
        group.appendChild(e);

//              <path d="M 100 100 L 300 100 L 200 300 z"
//        fill="red" stroke="blue" stroke-width="3" />

        e = document.createElementNS(svgNS, "path");
        e.setAttributeNS(null, "d", "M 100 100 L 300 100 L 200 300 z");
        e.setAttributeNS(null, "fill", "yellow");
        e.setAttributeNS(null, "stroke", "black");
        e.setAttributeNS(null, "stroke-width", "5");
        e.setAttributeNS(null, "fill-opacity", ".5");
        group.appendChild(e);

        return document;
    }

    public void save(Document document) throws Exception {
//        JPEGTranscoder t = new JPEGTranscoder();
//        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
//                             new Float(.8));
//        TranscoderInput input = new TranscoderInput(document);
//        OutputStream ostream = new FileOutputStream("/home/daniel/Desktop/out.jpg");
//        TranscoderOutput output = new TranscoderOutput(ostream);
//        t.transcode(input, output);
//        ostream.flush();
//        ostream.close();
        SVGTranscoder t = new SVGTranscoder();
        TranscoderInput input = new TranscoderInput(document);
        Writer writer = new FileWriter("/home/daniel/Desktop/out.svg");
        TranscoderOutput output = new TranscoderOutput(writer);
        t.transcode(input, output);
        writer.flush();
        writer.close();

    }

    public static void main(String [] args) throws Exception {
        SVGTest rasterizer = new SVGTest();
        Document document = rasterizer.createDocument();
        rasterizer.save(document);
        System.exit(0);
    }

}
