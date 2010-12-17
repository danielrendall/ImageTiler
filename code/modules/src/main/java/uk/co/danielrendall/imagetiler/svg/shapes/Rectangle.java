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

package uk.co.danielrendall.imagetiler.svg.shapes;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.TileContext;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 10:53:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rectangle extends BaseShape {
    private double x = 0.0d;
    private double y = 0.0d;
    private double width = 1.0d;
    private double height = 1.0d;

    public Rectangle() {
        super();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    protected Element _getElement(TileContext context) {
        if (width < 0.0d) {
            width = Math.abs(width);
            x = x - width;
        }
        if (height < 0.0d) {
            height = Math.abs(height);
            y = y - height;
        }
        Element e = context.createElement("rect");
        e.setAttributeNS(null, "x", string(x));
        e.setAttributeNS(null, "y", string(y));
        e.setAttributeNS(null, "width", string(width));
        e.setAttributeNS(null, "height", string(height));
        return e;
    }

}
