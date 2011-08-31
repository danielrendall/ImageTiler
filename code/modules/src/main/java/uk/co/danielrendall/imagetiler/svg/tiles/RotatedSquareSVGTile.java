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

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.BooleanParameter;
import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.*;
import uk.co.danielrendall.mathlib.geom2d.*;

import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 02-Apr-2010
 * Time: 10:17:46
 * To change this template use File | Settings | File Templates.
 */
@ClassDescriptor(name="RotatedSquare", description="Square on its point")
public class RotatedSquareSVGTile extends SimpleSVGTile {

    public final static Logger log = Logger.getLogger(StarSVGTile.class);

    private final double diamondAngle = Math.PI / 4.0d;

    @BooleanParameter(name = "invert", description = "Invert the sense of rotation (TODO - find name)", defaultValue = false)
    private boolean invert;
    @BooleanParameter(name = "diamond", description = "Base the tile on a diamond rather than a square", defaultValue = false)
    private boolean diamond;


    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

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

            Point center = context.getCenter();

            p.addPoint(tl);
            p.addPoint(bl);
            p.addPoint(br);
            p.addPoint(tr);

            p.rotate(context.getCenter(), (invert ? -context.getAngle() : context.getAngle()) + (diamond ? diamondAngle : 0.0d));

            group.appendChild(p.getElement(context));

            return true;
        }
        return false;
    }
    
}
