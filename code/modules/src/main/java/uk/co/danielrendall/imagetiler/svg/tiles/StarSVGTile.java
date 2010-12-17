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

package uk.co.danielrendall.imagetiler.svg.tiles;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
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
public class StarSVGTile extends SimpleSVGTile {
    public final static Logger log = Logger.getLogger(StarSVGTile.class);

    private static final String NAME_INNER_RADIUS = "innerRadius";
    private static final String DESCRIPTION_INNER_RADIUS = "Fractional inner radius of star";

    private static final String NAME_OUTER_RADIUS = "outerRadius";
    private static final String DESCRIPTION_OUTER_RADIUS = "Fractional outer radius of star";

    private final double increment = Math.PI / 5.0d;

    private final double innerRadius;
    private final double outerRadius;

    public StarSVGTile(
            @DoubleParameter(name = NAME_INSET, description = DESCRIPTION_INSET, defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
            double inset,
            @DoubleParameter(name = NAME_STROKE_WIDTH, description = DESCRIPTION_STROKE_WIDTH, defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
            double strokeWidth,
            @DoubleParameter(name = NAME_DARK_OPACITY, description = DESCRIPTION_DARK_OPACITY, defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
            double darkOpacity,
            @DoubleParameter(name = NAME_LIGHT_OPACITY, description = DESCRIPTION_LIGHT_OPACITY, defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
            double lightOpacity,
            @DoubleParameter(name = NAME_INNER_RADIUS, description = DESCRIPTION_INNER_RADIUS, defaultValue = 0.5d, minValue = 0.001d, maxValue = 10d)
            double innerRadius,
            @DoubleParameter(name = NAME_OUTER_RADIUS, description = DESCRIPTION_OUTER_RADIUS, defaultValue = 1.0d, minValue = 0.001d, maxValue = 10d)
            double outerRadius) {
        super(inset, strokeWidth, darkOpacity, lightOpacity);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
    }

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

}
