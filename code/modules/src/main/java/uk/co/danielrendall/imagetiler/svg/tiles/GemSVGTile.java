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
public class GemSVGTile extends SimpleSVGTile {

    public boolean getTile(Element group, TileContext context) {
        if (!context.getColor().equals(Color.WHITE)) {

            final double left = context.getLeft();
            final double right = context.getRight();
            final double top = context.getTop();
            final double bottom = context.getBottom();

            double inset = context.getDouble("inset", 0.1d);
            double sw = context.getDouble("strokewidth", 0.03d);
            double darkOpacity = context.getDouble("darkopacity", 0.8d);
            double lightOpacity = context.getDouble("lightopacity", 0.6d);

            Polygon p;
            p = new Polygon();
            p.setFill("");
            p.setStroke("black");
            p.setStrokeWidth(sw);

            Point center = context.getCenter();

            Point pLeft = new Point(left + inset, center.y());
            Point pTop = new Point(center.x(), top+inset);
            Point pRight = new Point(right - inset, center.y());
            Point pBottom = new Point(center.x(), bottom - inset);

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
