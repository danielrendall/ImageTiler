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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
import uk.co.danielrendall.imagetiler.annotations.IntegerParameter;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.*;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 21:08:22
 * To change this template use File | Settings | File Templates.
 */
@ClassDescriptor(name="Star", description="5-pointed star")
public class StarSVGTile extends SimpleSVGTile {
    public final static Logger log = LogManager.getLogger(StarSVGTile.class);

    // TODO - rotation angle offset? Some kind of skew angle to rotate inner points relative to outer?
    @DoubleParameter(name = "strokeWidth", description = "Width of stroke as fraction of tile size", defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
    private double strokeWidth;
    @DoubleParameter(name = "innerRadius", description = "Fractional inner radius of star", defaultValue = 0.5d, minValue = 0.001d, maxValue = 10d)
    private double innerRadius;
    @DoubleParameter(name = "outerRadius", description = "Fractional outer radius of star", defaultValue = 1.0d, minValue = 0.001d, maxValue = 10d)
    private double outerRadius;
    @IntegerParameter(name = "numberOfPoints", description = "Number of points", defaultValue = 5, minValue = 3, maxValue = 24)
    private int numberOfPoints;

    public boolean getTile(Element group, TileContext context) {
        double increment = Math.PI / (double) numberOfPoints;


        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            double inner = ((width + height) / 4.0d) * innerRadius;
            double outer = ((width + height) / 4.0d) * outerRadius;

            Polygon p = new Polygon();

            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(width * strokeWidth);
            p.setFillOpacity(1.0d);

            Point center = context.getCenter();

            for (int i=0; i<numberOfPoints; i++) {
                p.addPoint(center.displace(getVec(inner, -Math.PI + increment * (double) (2.0d * i))));
                p.addPoint(center.displace(getVec(outer, -Math.PI + increment * (double) (2.0d * i + 1.0d))));
            }

            p.rotate(context.getCenter(), context.getAngle());

            group.appendChild(p.getElement(context));

            return true;
        }
        return false;
    }

    private Vec getVec(double radius, double angle) {
        return new Vec(radius, 0).rotate(angle);
    }
}
