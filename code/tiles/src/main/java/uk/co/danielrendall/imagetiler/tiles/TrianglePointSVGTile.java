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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.BooleanParameter;
import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.*;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 16:05:34
 * To change this template use File | Settings | File Templates.
 */
@ClassDescriptor(name="TrianglePoint", description="Triangle with apex at center of image")
public class TrianglePointSVGTile extends SimpleSVGTile {
    public final static Logger log = LogManager.getLogger(TrianglePointSVGTile.class);


    @DoubleParameter(name = "strokeWidth", description = "Width of stroke as fraction of tile size", defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
    private double strokeWidth;
    @DoubleParameter(name = "weight", description = "How big to make the line", defaultValue = 1.0d, minValue = 0.01d, maxValue = 10d)
    private double weight;
    @DoubleParameter(name = "weightValue", description = "Affects dependence of line size on distance from center", defaultValue = 1.0d, minValue = 0.25d, maxValue = 1.25d)
    private double weightValue;

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            Point center = context.getCenter();
            double distance = context.getDistance();

            double myWeight =  weight * Math.pow(distance, weightValue);

            double radius = ((width + height) / 4.0d) * distance / myWeight;

            if (Double.isNaN(radius)) return false;

            Polygon p = new Polygon();
            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(width * strokeWidth);
            p.setFillOpacity(1.0d);

            Vec v1 = new Vec(radius, 0.0d).rotate(context.getAngle() + (Math.PI / 2.0));
            Vec v2 = new Vec(radius, 0.0d).rotate(context.getAngle() - (Math.PI / 2.0));

            p.addPoint(Point.ORIGIN);
            p.addPoint(center.displace(v1));
            p.addPoint(center.displace(v2));

//            log.info(p);
            group.appendChild(p.getElement(context));

            return true;
        }
        return false;
    }
}
