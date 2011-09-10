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
import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.svg.Shape;
import uk.co.danielrendall.imagetiler.svg.TileContext;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 10:51:18 PM
 * To change this template use File | Settings | File Templates.
 */

// The Polygon and Rectangle classes which extend this are both builders -
// one adds points to them and finally calls getElement to get the SVG element

public abstract class BaseShape implements Shape {

    private String fill = "";
    private String stroke = "";
    private double strokeWidth = 1.0d;
    private double fillOpacity = 1.0d;
    private String transform = "";

    public BaseShape() {

    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public void setStrokeWidth(double strokeWidth) {
        if (strokeWidth > 0.0d) {
            this.strokeWidth = strokeWidth;
        } else {
            Log.app.warn("Strokewidth shouldn't be 0");
            this.strokeWidth = 0.001;
        }
    }

    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    public Element getElement(TileContext context) {
        Element e = _getElement(context);
        if (!"".equals(fill)) {
            e.setAttributeNS(null, "fill", fill);
        }
        if (!"".equals(stroke)) {
            e.setAttributeNS(null, "stroke", stroke);
        }
        e.setAttributeNS(null, "stroke-width", string(strokeWidth));
        e.setAttributeNS(null, "fill-opacity", string(fillOpacity));
        e.setAttributeNS(null, "transform", transform);
        return e;
    }

    protected String string(double d) {
        return "" + d;
    }

    protected abstract Element _getElement(TileContext context);

    @Override
    public String toString() {
        return "BaseShape{" +
                "fill='" + fill + '\'' +
                ", stroke='" + stroke + '\'' +
                ", strokeWidth=" + strokeWidth +
                ", fillOpacity=" + fillOpacity +
                ", transform='" + transform + '\'' +
                '}';
    }
}
