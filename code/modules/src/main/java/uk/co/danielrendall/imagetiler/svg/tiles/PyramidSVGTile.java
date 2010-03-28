package uk.co.danielrendall.imagetiler.svg.tiles;

import org.w3c.dom.Element;
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

            double inset = context.getDouble("inset", 0.15d);
            double sw = context.getDouble("strokewidth", 0.03d);
            double darkOpacity = context.getDouble("darkopacity", 0.8d);
            double lightOpacity = context.getDouble("lightopacity", 0.6d);

            Point center = context.getCenter();
            Point tl = new Point(left + inset, top + inset);
            Point bl = new Point(left + inset, bottom - inset);
            Point tr = new Point(right - inset, top + inset);
            Point br = new Point(right - inset, bottom - inset);
            
            Polygon p = new Polygon();
            p.setFill("white");
            p.setStroke("black");
            p.setStrokeWidth(sw);
            
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
