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

package uk.co.danielrendall.imagetiler.svg.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.*;
import uk.co.danielrendall.imagetiler.svg.shapes.Polygon;
import uk.co.danielrendall.mathlib.geom2d.*;

import uk.co.danielrendall.mathlib.geom2d.Point;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Rendall
 */
public class LegoSVGTile extends SimpleSVGTile {
    /**
     *   <defs
     id="defs3151">
    <linearGradient
       id="linearGradient3923">
      <stop
         id="stop3931"
         offset="0"
         style="stop-color:#d2ca00;stop-opacity:1;" />
      <stop
         style="stop-color:#fff600;stop-opacity:1;"
         offset="1"
         id="stop3927" />
    </linearGradient>
    <linearGradient
       inkscape:collect="always"
       xlink:href="#linearGradient3923"
       id="linearGradient3929"
       x1="0.9375"
       y1="-2.5"
       x2="2.4425826"
       y2="-0.97650629"
       gradientUnits="userSpaceOnUse" />
    <linearGradient
       inkscape:collect="always"
       xlink:href="#linearGradient3923"
       id="linearGradient3951"
       x1="1.4393398"
       y1="-3"
       x2="3.0564513"
       y2="-1.4379368"
       gradientUnits="userSpaceOnUse" />
  </defs>

     ...
     <path
        d="M 1.4393398282201788 -3.560660171779821 L 2.439339828220179 -4.560660171779821 L 4.560660171779821 -2.4393398282201786 L 3.560660171779821 -1.4393398282201786 z"
        transform=""
        id="path3145"
        style="fill-opacity:1;fill:url(#linearGradient3951)"
        stroke-width="0.0"
        stroke="black"
        fill-opacity="1.0"
        fill="#fff600" />
     
     */

    private static final String NAME_BLOB_RADIUS = "blobRadius";
    private static final String DESCRIPTION_BLOB_RADIUS = "Radius of blob";

    private static final String NAME_BLOB_HEIGHT = "blobHeight";
    private static final String DESCRIPTION_BLOB_HEIGHT = "Height of the blob";

    private static final String NAME_TILE_DEPTH = "tileDepth";
    private static final String DESCRIPTION_TILE_DEPTH = "Depth of the lego tile";

    protected final double blobRadius;
    protected final double blobHeight;
    protected final double tileDepth;

    public LegoSVGTile(
            @DoubleParameter(name = NAME_INSET, description = DESCRIPTION_INSET, defaultValue=0.0d, minValue = 0.0d, maxValue = 0.5d)
            double inset,
            @DoubleParameter(name = NAME_STROKE_WIDTH, description = DESCRIPTION_STROKE_WIDTH, defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
            double strokeWidth,
            @DoubleParameter(name = NAME_DARK_OPACITY, description = DESCRIPTION_DARK_OPACITY, defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
            double darkOpacity,
            @DoubleParameter(name = NAME_LIGHT_OPACITY, description = DESCRIPTION_LIGHT_OPACITY, defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
            double lightOpacity,
            @DoubleParameter(name = NAME_BLOB_RADIUS, description = DESCRIPTION_BLOB_RADIUS, defaultValue=0.15d, minValue = 0.001d, maxValue = 0.25d)
            double blobRadius,
            @DoubleParameter(name = NAME_BLOB_HEIGHT, description = DESCRIPTION_BLOB_HEIGHT, defaultValue=0.1d, minValue = 0.001d, maxValue = 0.3d)
            double blobHeight,
            @DoubleParameter(name = NAME_TILE_DEPTH, description = DESCRIPTION_TILE_DEPTH, defaultValue=0.2d, minValue = 0.001d, maxValue = 0.5d)
            double tileDepth
    ) {
        super(inset, strokeWidth, darkOpacity, lightOpacity);
        this.blobRadius = blobRadius;
        this.blobHeight = blobHeight;
        this.tileDepth = tileDepth;
    }

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {

            double width = context.getWidth();
            double height = context.getHeight();

            double effectiveWidth = width * (1.0d - inset * 2.0d);
            double effectiveHeight = height * (1.0d - inset * 2.0d);
            Vec depthDisp = new Vec(-effectiveWidth * tileDepth, effectiveHeight*tileDepth);

            Vec blobDisp = new Vec(effectiveWidth*blobHeight, -effectiveHeight*blobHeight);

            Vec blobTlToBr = new Vec(2 * effectiveWidth * blobRadius / Math.sqrt(2.0d), 2 * effectiveHeight * blobRadius / Math.sqrt(2.0d));

            Polygon p = new Polygon();
            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(strokeWidth);
            p.setFillOpacity(1.0d);

            double effectiveLeft = context.getLeft() + (width * inset);
            double effectiveTop = context.getTop() + (height * inset);
            double effectiveBottom = context.getBottom() - (height * inset);
            double effectiveRight = context.getRight() - (width * inset);

            Point tl = new Point(effectiveLeft, effectiveTop);
            Point bl = new Point(effectiveLeft, effectiveBottom);
            Point tr = new Point(effectiveRight, effectiveTop);
            Point br = new Point(effectiveRight, effectiveBottom);

            p.addPoint(tl);
            p.addPoint(tr);
            p.addPoint(br);
            p.addPoint(bl);

            group.appendChild(p.getElement(context));
            p.clear();

            // Edge 1
            p.setFillOpacity(darkOpacity);
            p.addPoint(tl);
            p.addPoint(tl.displace(depthDisp));
            p.addPoint(bl.displace(depthDisp));
            p.addPoint(bl);
            group.appendChild(p.getElement(context));
            p.clear();

            // Edge 2
            p.setFillOpacity(lightOpacity);
            p.addPoint(bl);
            p.addPoint(bl.displace(depthDisp));
            p.addPoint(br.displace(depthDisp));
            p.addPoint(br);
            group.appendChild(p.getElement(context));
            p.clear();

            List<Point> blobList = new ArrayList<Point>();

            blobList.add(bl.displace(new Vec(effectiveWidth / 4.0, -effectiveHeight/4.0)));
            blobList.add(tl.displace(new Vec(effectiveWidth / 4.0, effectiveHeight/4.0)));
            blobList.add(br.displace(new Vec(-effectiveWidth / 4.0, -effectiveHeight/4.0)));
            blobList.add(tr.displace(new Vec(-effectiveWidth / 4.0, effectiveHeight/4.0)));

            Circle c = new Circle();
            c.setFill(hexValue(context.getColor()));
            c.setStroke("black");
            c.setStrokeWidth(strokeWidth);
            c.setFillOpacity(1.0);
            c.setRadius(effectiveWidth * blobRadius);

            StraightLine line = new StraightLine();
            line.setFill(hexValue(context.getColor()));
            line.setStroke("black");
            line.setStrokeWidth(strokeWidth);
            line.setFillOpacity(1.0);

            p.setStrokeWidth(0.0d);
            p.setFillOpacity(1.0d);
            for (int i = 0; i < blobList.size(); i++) {
                Point centre = blobList.get(i);
                Point displacedCentre = centre.displace(blobDisp);
                Point lowerCircleTopLeft = centre.displace(blobTlToBr.scale(0.5).neg());
                Point upperCircleTopLeft = lowerCircleTopLeft.displace(blobDisp);
                Point upperCircleBottomRight = upperCircleTopLeft.displace(blobTlToBr);
                Point lowerCircleBottomRight = upperCircleBottomRight.displace(blobDisp.neg());
                
                // lower circle
                c.setCentre(centre);
                group.appendChild(c.getElement(context));

                // rotated rectangular filling blob.
                p.addPoint(lowerCircleTopLeft);
                p.addPoint(upperCircleTopLeft);
                p.addPoint(upperCircleBottomRight);
                p.addPoint(lowerCircleBottomRight);
                group.appendChild(p.getElement(context));
                p.clear();

                // upper circle
                c.setCentre(displacedCentre);
                group.appendChild(c.getElement(context));

                line.setStart(lowerCircleTopLeft);
                line.setEnd(upperCircleTopLeft);
                group.appendChild(line.getElement(context));

                line.setStart(lowerCircleBottomRight);
                line.setEnd(upperCircleBottomRight);
                group.appendChild(line.getElement(context));

            }





            return true;
        }
        return false;
    }
}
