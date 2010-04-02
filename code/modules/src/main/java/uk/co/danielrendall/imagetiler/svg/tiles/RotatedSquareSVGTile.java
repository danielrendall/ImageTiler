package uk.co.danielrendall.imagetiler.svg.tiles;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
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
public class RotatedSquareSVGTile extends SimpleSVGTile {

    public final static Logger log = Logger.getLogger(StarSVGTile.class);
    private final double diamondAngle = Math.PI / 4.0d;

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            double inset = context.getDouble("inset", 0.1d);
            double sw = context.getDouble("strokewidth", 0.03d);
            boolean invert = context.getBoolean("invert", false);
            boolean diamond = context.getBoolean("diamond", false);

            Polygon p = new Polygon();

            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(sw);
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
