package uk.co.danielrendall.imagetiler.svg.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
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

    private static final String NAME_COLUMN_STROKE_WIDTH = "columnStrokeWidth";
    private static final String DESCRIPTION_COLUMN_STROKE_WIDTH = "Width of stroke for column as fraction of tile size";

    protected final double columnStrokeWidth;

    public SquareExtrudedSVGTile(
            @DoubleParameter(name = NAME_INSET, description = DESCRIPTION_INSET, defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
            double inset,
            @DoubleParameter(name = NAME_STROKE_WIDTH, description = DESCRIPTION_STROKE_WIDTH, defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
            double strokeWidth,
            @DoubleParameter(name = NAME_DARK_OPACITY, description = DESCRIPTION_DARK_OPACITY, defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
            double darkOpacity,
            @DoubleParameter(name = NAME_LIGHT_OPACITY, description = DESCRIPTION_LIGHT_OPACITY, defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
            double lightOpacity,
            @DoubleParameter(name = NAME_COLUMN_STROKE_WIDTH, description = DESCRIPTION_COLUMN_STROKE_WIDTH, defaultValue=0.01d, minValue = 0.001d, maxValue = 0.5d)
            double columnStrokeWidth) {
        super(inset, strokeWidth, darkOpacity, lightOpacity);
        this.columnStrokeWidth = columnStrokeWidth;
    }

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            Polygon p = new Polygon();
            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(columnStrokeWidth);
            p.setFillOpacity(darkOpacity);

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
            r.setStrokeWidth(strokeWidth);
            Element e = r.getElement(context);
            group.appendChild(e);

            return true;
        }
        return false;
    }

}
