package uk.co.danielrendall.imagetiler.svg.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
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
    
    public GemSVGTile(
            @DoubleParameter(name = NAME_INSET, description = DESCRIPTION_INSET, defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
            double inset,
            @DoubleParameter(name = NAME_STROKE_WIDTH, description = DESCRIPTION_STROKE_WIDTH, defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
            double strokeWidth,
            @DoubleParameter(name = NAME_DARK_OPACITY, description = DESCRIPTION_DARK_OPACITY, defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
            double darkOpacity,
            @DoubleParameter(name = NAME_LIGHT_OPACITY, description = DESCRIPTION_LIGHT_OPACITY, defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
            double lightOpacity) {
        super(inset, strokeWidth, darkOpacity, lightOpacity);
    }

    public boolean getTile(Element group, TileContext context) {
        if (!context.getColor().equals(Color.WHITE)) {

            final double left = context.getLeft();
            final double right = context.getRight();
            final double top = context.getTop();
            final double bottom = context.getBottom();

            Polygon p;
            p = new Polygon();
            p.setFill("");
            p.setStroke("black");
            p.setStrokeWidth(strokeWidth);

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
