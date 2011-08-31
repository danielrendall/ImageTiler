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
public class PyramidSVGTile extends SimpleSVGTile {

    public boolean getTile(Element group, TileContext context) {
        if (!context.getColor().equals(Color.WHITE)) {

            final double left = context.getLeft();
            final double right = context.getRight();
            final double top = context.getTop();
            final double bottom = context.getBottom();

            Point center = context.getCenter();
            Point tl = new Point(left + inset, top + inset);
            Point bl = new Point(left + inset, bottom - inset);
            Point tr = new Point(right - inset, top + inset);
            Point br = new Point(right - inset, bottom - inset);
            
            Polygon p = new Polygon();
            p.setFill("white");
            p.setStroke("black");
            p.setStrokeWidth(strokeWidth);
            
            p.addPoint(tl);
            p.addPoint(tr);
            p.addPoint(br);
            p.addPoint(bl);
            group.appendChild(p.getElement(context));

            p.clear();
            p.setFill(hexValue(context.getColor()));
            p.setFillOpacity(lightOpacity);
            p.setStroke("");
            p.setStrokeWidth(0);

            p.addPoint(tl);
            p.addPoint(tr);
            p.addPoint(center);
            group.appendChild(p.getElement(context));

            p.setFillOpacity(darkOpacity);
            p.clear();
            p.addPoint(bl);
            p.addPoint(tl);
            p.addPoint(center);
            group.appendChild(p.getElement(context));

            p.clear();
            p.addPoint(tr);
            p.addPoint(br);
            p.addPoint(center);
            group.appendChild(p.getElement(context));

            p.setFillOpacity(1.0);
            p.clear();
            p.addPoint(br);
            p.addPoint(bl);
            p.addPoint(center);
            group.appendChild(p.getElement(context));
            return true;
        } else {
            return false;
        }

    }

}
