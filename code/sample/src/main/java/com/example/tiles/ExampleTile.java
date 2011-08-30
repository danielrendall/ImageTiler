package com.example.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
import uk.co.danielrendall.imagetiler.svg.SVGTile;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.*;

import java.awt.*;
import java.awt.Rectangle;

/**
 * @author Daniel Rendall
 */
public class ExampleTile implements SVGTile {

    protected static final String NAME_INSET = "inset";
    protected static final String DESCRIPTION_INSET = "Fractional inset";

    protected static final String NAME_STROKE_WIDTH = "strokeWidth";
    protected static final String DESCRIPTION_STROKE_WIDTH = "Width of stroke as fraction of tile size";

    protected static final String NAME_DARK_OPACITY = "darkOpacity";
    protected static final String DESCRIPTION_DARK_OPACITY = "Opacity of dark areas";

    protected static final String NAME_LIGHT_OPACITY = "lightOpacity";
    protected static final String DESCRIPTION_LIGHT_OPACITY = "Opacity of light areas";

    protected static final String NAME_EXAMPLE_PROPERTY = "exampleProperty";
    protected static final String DESCRIPTION_EXAMPLE_PROPERTY = "An example property";

    protected final double inset;
    protected final double strokeWidth;
    protected final double darkOpacity;
    protected final double lightOpacity;
    protected final double exampleProperty;

    public ExampleTile(
            @DoubleParameter(name = NAME_INSET, description = DESCRIPTION_INSET, defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
            double inset,
            @DoubleParameter(name = NAME_STROKE_WIDTH, description = DESCRIPTION_STROKE_WIDTH, defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
            double strokeWidth,
            @DoubleParameter(name = NAME_DARK_OPACITY, description = DESCRIPTION_DARK_OPACITY, defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
            double darkOpacity,
            @DoubleParameter(name = NAME_LIGHT_OPACITY, description = DESCRIPTION_LIGHT_OPACITY, defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
            double lightOpacity,
            @DoubleParameter(name = NAME_EXAMPLE_PROPERTY, description = DESCRIPTION_EXAMPLE_PROPERTY, defaultValue=5.0d, minValue = 0.0d, maxValue = 10.0d)
            double exampleProperty) {
        this.inset = inset;
        this.strokeWidth = strokeWidth;
        this.darkOpacity = darkOpacity;
        this.lightOpacity = lightOpacity;
        this.exampleProperty = exampleProperty;
    }
    
    public boolean getTile(Element group, TileContext context) {
        if (!context.getColor().equals(Color.WHITE)) {
            uk.co.danielrendall.imagetiler.svg.shapes.Rectangle r = new uk.co.danielrendall.imagetiler.svg.shapes.Rectangle();
            double width = context.getWidth();
            double height = context.getHeight();

            r.setX(context.getLeft() + (width * inset));
            r.setY(context.getTop() + (height * inset));
            r.setWidth(width * (1.0d - 2.0d * inset));
            r.setHeight(height * (1.0d - 2.0d * inset));
            r.setFill("#ff0000");
            r.setStroke("black");
            r.setStrokeWidth(width * strokeWidth);
            Element e = r.getElement(context);
            group.appendChild(e);
            return true;
        }
        return false;
    }
}
