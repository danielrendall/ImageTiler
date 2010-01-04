package uk.co.danielrendall.imagetiler.svg;

import org.w3c.dom.Element;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 9:37:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TileContext {

    private double left;
    private double right;
    private double top;
    private double bottom;
    private Color color;
    private SVGTiler tiler;

    public TileContext(double left, double right, double top, double bottom, Color color, SVGTiler tiler) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.color = color;
        this.tiler = tiler;
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

    public final double getMidWidth() {
        return (left + right) / 2.0d;
    }

    public final double getMidHeight() {
        return (top + bottom) / 2.0d;
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
}
