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
import uk.co.danielrendall.imagetiler.svg.shapes.Polygon;
import uk.co.danielrendall.mathlib.geom2d.Point;

import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 10:41:54 PM
 * To change this template use File | Settings | File Templates.
 */
@ClassDescriptor(name="Gem", description="A four-colour diamond")
public class GemSVGTile extends SimpleSVGTile {

    @DoubleParameter(name = "inset", description = "Fractional inset", defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
    private double inset;
    @DoubleParameter(name = "strokeWidth", description = "Width of stroke as fraction of tile size", defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
    private double strokeWidth;
    @DoubleParameter(name = "darkOpacity", description = "Opacity of dark areas", defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
    private double darkOpacity;
    @DoubleParameter(name = "lightOpacity", description = "Opacity of light areas", defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
    private double lightOpacity;
    
    public boolean getTile(Element group, TileContext context) {
        if (!context.getColor().equals(Color.WHITE)) {

            final double left = context.getLeft();
            final double right = context.getRight();
            final double top = context.getTop();
            final double bottom = context.getBottom();


            Polygon p;
            p = new Polygon();
            p.setFill("");
            p.setStroke("black");
            p.setStrokeWidth(context.getWidth() * strokeWidth);

            Point center = context.getCenter();

            Point pLeft = new Point(left + context.getWidth() * inset, center.y());
            Point pTop = new Point(center.x(), top + context.getHeight() * inset);
            Point pRight = new Point(right - context.getWidth() * inset, center.y());
            Point pBottom = new Point(center.x(), bottom - context.getHeight() * inset);

            p.addPoint(pLeft);
            p.addPoint(pTop);
            p.addPoint(pRight);
            p.addPoint(pBottom);
            group.appendChild(p.getElement(context));

            p.clear();
            p.setFill(hexValue(context.getColor()));
            p.setStroke("");
            p.setStrokeWidth(0);

            p.addPoint(pLeft);
            p.addPoint(pTop);
            p.addPoint(center);
            group.appendChild(p.getElement(context));

            p.setFillOpacity(darkOpacity);
            p.clear();
            p.addPoint(pLeft);
            p.addPoint(pBottom);
            p.addPoint(center);
            group.appendChild(p.getElement(context));

            p.clear();
            p.addPoint(pRight);
            p.addPoint(pTop);
            p.addPoint(center);
            group.appendChild(p.getElement(context));

            p.setFillOpacity(lightOpacity);
            p.clear();
            p.addPoint(pRight);
            p.addPoint(pBottom);
            p.addPoint(center);
            group.appendChild(p.getElement(context));

            return true;
        } else {
            return false;
        }
    }
}
