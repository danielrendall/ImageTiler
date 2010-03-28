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
 * Time: 21:08:22
 * To change this template use File | Settings | File Templates.
 */
public class StarSVGTile extends SimpleSVGTile {
    public final static Logger log = Logger.getLogger(StarSVGTile.class);
    private final double increment = Math.PI / 5.0d;

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            double inset = context.getDouble("inset", 0.1d);
            double sw = context.getDouble("strokewidth", 0.03d);
            double inner = context.getDouble("innerradius", 0.5d);
            double outer = context.getDouble("outerradius", 1.0d);

            double innerRadius = ((width + height) / 4.0d) * inner ;
            double outerRadius = ((width + height) / 4.0d) * outer ;

            Polygon p = new Polygon();

            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(sw);
            p.setFillOpacity(1.0d);

            Point center = context.getCenter();

            p.addPoint(center.displace(getVec(innerRadius, -Math.PI)));
            p.addPoint(center.displace(getVec(outerRadius, -Math.PI + increment)));
            p.addPoint(center.displace(getVec(innerRadius, -Math.PI + 2.0d * increment)));
            p.addPoint(center.displace(getVec(outerRadius, -Math.PI + 3.0d * increment)));
            p.addPoint(center.displace(getVec(innerRadius, -Math.PI + 4.0d * increment)));
            p.addPoint(center.displace(getVec(outerRadius, -Math.PI + 5.0d * increment)));
            p.addPoint(center.displace(getVec(innerRadius, -Math.PI + 6.0d * increment)));
            p.addPoint(center.displace(getVec(outerRadius, -Math.PI + 7.0d * increment)));
            p.addPoint(center.displace(getVec(innerRadius, -Math.PI + 8.0d * increment)));
            p.addPoint(center.displace(getVec(outerRadius, -Math.PI + 9.0d * increment)));

            p.rotate(context.getCenter(), context.getAngle());

            group.appendChild(p.getElement(context));

            return true;
        }
        return false;
    }

    private Vec getVec(double radius, double angle) {
        return new Vec(radius, 0).rotate(angle);
    }

}
