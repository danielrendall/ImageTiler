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

package uk.co.danielrendall.imagetiler.svg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.co.danielrendall.mathlib.geom2d.Compass;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 9:37:09 PM
 * To change this template use File | Settings | File Templates.
 */

// Class is a facade, giving the SVGTile implementation a simple way of accessing
// various pieces of information which may be properties of the location, computed
// properties, or command-line settings
public class TileContext {

    public final static Logger log = LogManager.getLogger(TileContext.class);

    private final double left;
    private final double right;
    private final double top;
    private final double bottom;
    private final Point center;
    private final double angle;
    private final Color color;
    private final Document document;
    private final SVGTiler tiler;
    private final double distance;

    public TileContext(double left, double right, double top, double bottom, Color color, Document document, SVGTiler tiler) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.center = new Point(((left + right) / 2.0d), ((top + bottom) / 2.0d));
        this.color = color;
        this.document = document;
        this.tiler = tiler;
        this.distance = Math.sqrt(this.center.x() * this.center.x() + this.center.y() * this.center.y());
        new Vec(center).angle();
        angle = new Vec(center).angle();
    }

    public final double getLeft() {
        return left;
    }

    public final double getRight() {
        return right;
    }

    public final double getTop() {
        return top;
    }

    public final double getBottom() {
        return bottom;
    }

    public final Point getCenter() {
        return center;
    }

    public final  Color getColor() {
        return color;
    }

    public double getDistance() {
        return distance;
    }

    public final Element createElement(String name) {
        return tiler.createElement(document, name);
    }

    public final double getWidth() {
        return tiler.getScale();
    }

    public final double getHeight() {
        return tiler.getScale();
    }

    public final double getAngle() {
        return angle;
    }

    public Compass getOctant() {
        return center.getOctant();
    }

    public Compass getQuadrant() {
        return center.getQuadrant();
    }
}
