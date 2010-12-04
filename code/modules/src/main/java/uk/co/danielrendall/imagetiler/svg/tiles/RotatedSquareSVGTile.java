package uk.co.danielrendall.imagetiler.svg.tiles;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.BooleanParameter;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
import uk.co.danielrendall.imagetiler.annotations.IntegerParameter;
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

    private static final String NAME_INVERT = "invert";
    private static final String DESCRIPTION_INVERT = "Invert the sense of rotation (TODO - find name)";

    private static final String NAME_DIAMOND = "diamond";
    private static final String DESCRIPTION_DIAMOND = "Base the tile on a diamond rather than a square";

    private final double diamondAngle = Math.PI / 4.0d;

    private final boolean invert;
    private final boolean diamond;

    public RotatedSquareSVGTile(
            @DoubleParameter(name = NAME_INSET, description = DESCRIPTION_INSET, defaultValue=0.15d, minValue = -2.0d, maxValue = 2.0d)
            double inset,
            @DoubleParameter(name = NAME_STROKE_WIDTH, description = DESCRIPTION_STROKE_WIDTH, defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
            double strokeWidth,
            @DoubleParameter(name = NAME_DARK_OPACITY, description = DESCRIPTION_DARK_OPACITY, defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
            double darkOpacity,
            @DoubleParameter(name = NAME_LIGHT_OPACITY, description = DESCRIPTION_LIGHT_OPACITY, defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
            double lightOpacity,
            @BooleanParameter(name = NAME_INVERT, description = DESCRIPTION_INVERT, defaultValue = false)
            boolean invert,
            @BooleanParameter(name = NAME_DIAMOND, description = DESCRIPTION_DIAMOND, defaultValue = false)
            boolean diamond
    ) {
        super(inset, strokeWidth, darkOpacity, lightOpacity);
        this.invert = invert;
        this.diamond = diamond;
    }

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();

            Polygon p = new Polygon();

            p.setFill(hexValue(context.getColor()));
            p.setStroke("black");
            p.setStrokeWidth(strokeWidth);
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
