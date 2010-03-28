package uk.co.danielrendall.imagetiler.svg.tiles;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.*;
import uk.co.danielrendall.mathlib.geom2d.Point;
import uk.co.danielrendall.mathlib.geom2d.Vec;

import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 16:05:34
 * To change this template use File | Settings | File Templates.
 */
public class TrianglePointSVGTile extends SimpleSVGTile {
    public final static Logger log = Logger.getLogger(TrianglePointSVGTile.class);

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            double sw = context.getDouble("strokewidth", 0.03d);

            Point center = context.getCenter();
            double distance = context.getDistance();

            double weight = context.getDouble("weight", distance);

            double radius = ((width + height) / 4.0d) * distance / weight ;

            Polygon p = new Polygon();
            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(sw);
            p.setFillOpacity(1.0d);

            Vec v1 = new Vec(radius, 0.0d).rotate(context.getAngle() + (Math.PI / 2.0));
            Vec v2 = new Vec(radius, 0.0d).rotate(context.getAngle() - (Math.PI / 2.0));

            p.addPoint(Point.ORIGIN);
            p.addPoint(center.displace(v1));
            p.addPoint(center.displace(v2));

//            log.info(p);
            group.appendChild(p.getElement(context));

            return true;
        }
        return false;
    }

}
