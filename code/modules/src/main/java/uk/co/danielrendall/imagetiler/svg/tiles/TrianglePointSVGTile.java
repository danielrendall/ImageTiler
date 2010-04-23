package uk.co.danielrendall.imagetiler.svg.tiles;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.BooleanParameter;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
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


    private final double weight;
    private final boolean contextWeighting;

    public TrianglePointSVGTile(
            @DoubleParameter(name = NAME_INSET, description = DESCRIPTION_INSET, defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
            double inset,
            @DoubleParameter(name = NAME_STROKE_WIDTH, description = DESCRIPTION_STROKE_WIDTH, defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
            double strokeWidth,
            @DoubleParameter(name = NAME_DARK_OPACITY, description = DESCRIPTION_DARK_OPACITY, defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
            double darkOpacity,
            @DoubleParameter(name = NAME_LIGHT_OPACITY, description = DESCRIPTION_LIGHT_OPACITY, defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
            double lightOpacity,
            @DoubleParameter(name = "weight", description = "How big to make the line", defaultValue = 1.0d, minValue = 0.01d, maxValue = 10d)
            double weight,
            @BooleanParameter(name = "contextWeighting", description = "Whether to weight by distance from center", defaultValue = true)
            boolean contextWeighting) {
        super(inset, strokeWidth, darkOpacity, lightOpacity);
        this.weight = weight;
        this.contextWeighting = contextWeighting;
    }

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            Point center = context.getCenter();
            double distance = context.getDistance();

            double myWeight = contextWeighting? (weight *  distance) : weight;

            double radius = ((width + height) / 4.0d) * distance / myWeight;

            Polygon p = new Polygon();
            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(strokeWidth);
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
