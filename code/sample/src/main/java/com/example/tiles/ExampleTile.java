package com.example.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
import uk.co.danielrendall.imagetiler.svg.SVGTile;
import uk.co.danielrendall.imagetiler.svg.TileContext;

import java.awt.*;

/**
 * @author Daniel Rendall
 */
public class ExampleTile extends SVGTile {

    @DoubleParameter(name = "inset", description = "Fractional inset", defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
    protected double inset;
    @DoubleParameter(name = "strokeWidth", description = "Width of stroke as fraction of tile size", defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
    protected double strokeWidth;
    @DoubleParameter(name = "darkOpacity", description = "Opacity of dark areas", defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
    protected double darkOpacity;
    @DoubleParameter(name = "lightOpacity", description = "Opacity of light areas", defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
    protected double lightOpacity;
    @DoubleParameter(name = "exampleProperty", description = "An example property", defaultValue=5.0d, minValue = 0.0d, maxValue = 10.0d)
    protected double exampleProperty;


    public boolean getTile(Element group, TileContext context) {
        if (!context.getColor().equals(Color.WHITE)) {           uk.co.danielrendall.imagetiler.svg.shapes.Rectangle r = new uk.co.danielrendall.imagetiler.svg.shapes.Rectangle();
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
