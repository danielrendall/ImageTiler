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

package uk.co.danielrendall.imagetiler.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
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
@ClassDescriptor(name="Lego", description="Thin 2x2 lego block")
public class LegoSVGTile extends SimpleSVGTile {

    @DoubleParameter(name = "inset", description = "Fractional inset", defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
    private double inset;
    @DoubleParameter(name = "strokeWidth", description = "Width of stroke as fraction of tile size", defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
    private double strokeWidth;
    @DoubleParameter(name = "darkOpacity", description = "Opacity of dark areas", defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
    private double darkOpacity;
    @DoubleParameter(name = "lightOpacity", description = "Opacity of light areas", defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
    private double lightOpacity;
    @DoubleParameter(name = "blobRadius", description = "Radius of blob", defaultValue=0.15d, minValue = 0.001d, maxValue = 0.25d)
    private double blobRadius;
    @DoubleParameter(name = "blobHeight", description = "Height of the blob", defaultValue=0.1d, minValue = 0.001d, maxValue = 0.3d)
    private double blobHeight;
    @DoubleParameter(name = "tileDepth", description = "Depth of the lego tile", defaultValue=0.2d, minValue = 0.001d, maxValue = 0.5d)
    private double tileDepth;

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
            c.setStrokeWidth(width * strokeWidth);
            c.setFillOpacity(1.0);
            c.setRadius(effectiveWidth * blobRadius);

            StraightLine line = new StraightLine();
            line.setFill(hexValue(context.getColor()));
            line.setStroke("black");
            line.setStrokeWidth(width * strokeWidth);
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
