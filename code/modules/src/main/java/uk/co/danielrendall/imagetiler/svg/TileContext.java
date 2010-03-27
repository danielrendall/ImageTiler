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

    public static enum Compass {NE, NW, SW, SE, N, S, E, W, CENTER}

    private final double OCT1 = -7.0d * Math.PI / 8.0d;
    private final double OCT2 = -5.0d * Math.PI / 8.0d;
    private final double OCT3 = -3.0d * Math.PI / 8.0d;
    private final double OCT4 = -1.0d * Math.PI / 8.0d;
    private final double OCT5 = 1.0d * Math.PI / 8.0d;
    private final double OCT6 = 3.0d * Math.PI / 8.0d;
    private final double OCT7 = 5.0d * Math.PI / 8.0d;
    private final double OCT8 = 7.0d * Math.PI / 8.0d;

    private final double QUAD1 = -Math.PI / 2.0d;
    private final double QUAD2 = 0;
    private final double QUAD3 = Math.PI / 2.0d;
    private final double QUAD4 = Math.PI;

    private final double left;
    private final double right;
    private final double top;
    private final double bottom;
    private final double midWidth;
    private final double midHeight;
    private final Color color;
    private final SVGTiler tiler;

    public TileContext(double left, double right, double top, double bottom, Color color, SVGTiler tiler) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.midWidth = (left + right) / 2.0d;
        this.midHeight = (top + bottom) / 2.0d;
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
        return midWidth;
    }

    public final double getMidHeight() {
        return midHeight;
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

    public Compass getOctant() {
        if ((midWidth == 0.0d) && (midHeight == 0.0d)) return Compass.CENTER;
        double tan = Math.atan2(midHeight, midWidth);
        if (tan < OCT1) return Compass.W;
        if (tan < OCT2) return Compass.NW;
        if (tan < OCT3) return Compass.N;
        if (tan < OCT4) return Compass.NE;
        if (tan < OCT5) return Compass.E;
        if (tan < OCT6) return Compass.SE;
        if (tan < OCT7) return Compass.S;
        if (tan < OCT8) return Compass.SW;
        return Compass.W;
    }

    public Compass getQuadrant() {
        if (midWidth == 0.0d) {
            if ((midHeight == 0.0d)) return Compass.CENTER;
            if (midHeight < 0.0d) return Compass.N; else return Compass.S;
        } else if (midHeight == 0.0d) {
            if (midWidth < 0.0d) return Compass.W; else return Compass.E;
        }
        double tan = Math.atan2(getMidHeight(), getMidWidth());
        if (tan < QUAD1) return Compass.NW;
        if (tan < QUAD2) return Compass.NE;
        if (tan < QUAD3) return Compass.SE;
        return Compass.SW;
    }
}
