package uk.co.danielrendall.imagetiler.svg;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
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
public class TileContext {

    public final static Logger log = Logger.getLogger(TileContext.class);

    private final double left;
    private final double right;
    private final double top;
    private final double bottom;
    private final Point center;
    private final double angle;
    private final Color color;
    private final SVGTiler tiler;

    public TileContext(double left, double right, double top, double bottom, Color color, SVGTiler tiler) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.center = new Point(((left + right) / 2.0d), ((top + bottom) / 2.0d));
        this.color = color;
        this.tiler = tiler;
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

    public final Element createElement(String name) {
        return tiler.createElement(name);
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

    public Point.Compass getOctant() {
        return center.getOctant();
    }

    public Point.Compass getQuadrant() {
        return center.getQuadrant();
    }
}
