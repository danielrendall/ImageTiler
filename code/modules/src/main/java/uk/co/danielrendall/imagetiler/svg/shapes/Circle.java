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

package uk.co.danielrendall.imagetiler.svg.shapes;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.mathlib.geom2d.Point;

/**
 * @author Daniel Rendall
 */
public class Circle extends BaseShape {

    private Point centre;
    private double radius;

    public Circle() {
        super();
    }

    public void setCentre(Point centre) {
        this.centre = centre;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    protected Element _getElement(TileContext context) {
        Element e = context.createElement("circle");
        e.setAttributeNS(null, "cx", Double.toString(centre.x()));
        e.setAttributeNS(null, "cy", Double.toString(centre.y()));
        e.setAttributeNS(null, "r", Double.toString(radius));
        return e;
    }
}
