package uk.co.danielrendall.imagetiler.svg.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.*;
import uk.co.danielrendall.mathlib.geom2d.Point;

import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 12:38:16
 * To change this template use File | Settings | File Templates.
 */
public class SquareExtrudedSVGTile extends SimpleSVGTile {

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();
            double inset = context.getDouble("inset", 0.15d);
            double sw = context.getDouble("strokewidth", 0.03d);
            double csw = context.getDouble("columnstrokewidth", 0.008d);
            double opacity = context.getDouble("opacity", 0.8d);

            Polygon p = new Polygon();
            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(csw);
            p.setFillOpacity(opacity);

            double effectiveLeft = context.getLeft() + (width * inset);
            double effectiveTop = context.getTop() + (height * inset);
            double effectiveBottom = context.getBottom() - (height * inset);
            double effectiveRight = context.getRight() - (width * inset);

            Point tl = new Point(effectiveLeft, effectiveTop);
            Point bl = new Point(effectiveLeft, effectiveBottom);
            Point tr = new Point(effectiveRight, effectiveTop);
            Point br = new Point(effectiveRight, effectiveBottom);
                    

            switch (context.getQuadrant()) {

                case NE:
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(bl);
                    p.addPoint(tl);
                    group.appendChild(p.getElement(context));
                    p.clear();
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(tr);
                    p.addPoint(tl);
                    group.appendChild(p.getElement(context));
                    break;
                case N:
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(tr);
                    p.addPoint(tl);
                    group.appendChild(p.getElement(context));
                    break;
                case NW:
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(br);
                    p.addPoint(tr);
                    group.appendChild(p.getElement(context));
                    p.clear();
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(tl);
                    p.addPoint(tr);
                    group.appendChild(p.getElement(context));
                    break;
                case W:
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(br);
                    p.addPoint(tr);
                    group.appendChild(p.getElement(context));
                    break;
                case SW:
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(tr);
                    p.addPoint(br);
                    group.appendChild(p.getElement(context));
                    p.clear();
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(bl);
                    p.addPoint(br);
                    group.appendChild(p.getElement(context));
                    break;
                case S:
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(bl);
                    p.addPoint(br);
                    group.appendChild(p.getElement(context));
                    break;
                case SE:
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(tl);
                    p.addPoint(bl);
                    group.appendChild(p.getElement(context));
                    p.clear();
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(br);
                    p.addPoint(bl);
                    group.appendChild(p.getElement(context));
                    break;
                case E:
                    p.addPoint(Point.ORIGIN);
                    p.addPoint(tl);
                    p.addPoint(bl);
                    group.appendChild(p.getElement(context));
                    break;
                default:
                    break;
            }

            Rectangle r = new Rectangle();
            r.setX(effectiveLeft);
            r.setY(effectiveTop);
            r.setWidth(effectiveRight - effectiveLeft);
            r.setHeight(effectiveBottom - effectiveTop);
            r.setFill(hexValue(context.getColor()));
            r.setStroke("black");
            r.setStrokeWidth(sw);
            Element e = r.getElement(context);
            group.appendChild(e);

            return true;
        }
        return false;
    }

}
