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

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
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
 * Time: 21:08:22
 * To change this template use File | Settings | File Templates.
 */
@ClassDescriptor(name="Star", description="5-pointed star")
public class StarSVGTile extends SimpleSVGTile {
    public final static Logger log = Logger.getLogger(StarSVGTile.class);

    // TODO - make this configurable, also the initial angle
    private final double increment = Math.PI / 5.0d;

    @DoubleParameter(name = "innerRadius", description = "Fractional inner radius of star", defaultValue = 0.5d, minValue = 0.001d, maxValue = 10d)
    private double innerRadius;
    @DoubleParameter(name = "outerRadius", description = "Fractional outer radius of star", defaultValue = 1.0d, minValue = 0.001d, maxValue = 10d)
    private double outerRadius;


    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            double inner = ((width + height) / 4.0d) * innerRadius;
            double outer = ((width + height) / 4.0d) * outerRadius;

            Polygon p = new Polygon();

            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(strokeWidth);
            p.setFillOpacity(1.0d);

            Point center = context.getCenter();

            p.addPoint(center.displace(getVec(inner, -Math.PI)));
            p.addPoint(center.displace(getVec(outer, -Math.PI + increment)));
            p.addPoint(center.displace(getVec(inner, -Math.PI + 2.0d * increment)));
            p.addPoint(center.displace(getVec(outer, -Math.PI + 3.0d * increment)));
            p.addPoint(center.displace(getVec(inner, -Math.PI + 4.0d * increment)));
            p.addPoint(center.displace(getVec(outer, -Math.PI + 5.0d * increment)));
            p.addPoint(center.displace(getVec(inner, -Math.PI + 6.0d * increment)));
            p.addPoint(center.displace(getVec(outer, -Math.PI + 7.0d * increment)));
            p.addPoint(center.displace(getVec(inner, -Math.PI + 8.0d * increment)));
            p.addPoint(center.displace(getVec(outer, -Math.PI + 9.0d * increment)));

            p.rotate(context.getCenter(), context.getAngle());

            group.appendChild(p.getElement(context));

            return true;
        }
        return false;
    }

    private Vec getVec(double radius, double angle) {
        return new Vec(radius, 0).rotate(angle);
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(double innerRadius) {
        this.innerRadius = helper.check("innerRadius", innerRadius);
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(double outerRadius) {
        this.outerRadius = helper.check("outerRadius", outerRadius);
    }
}
