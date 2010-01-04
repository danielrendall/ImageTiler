package uk.co.danielrendall.imagetiler.svg.shapes;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.Shape;
import uk.co.danielrendall.imagetiler.svg.TileContext;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 10:51:18 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseShape implements Shape {

    private String fill = "";
    private String stroke = "";
    private double strokeWidth = 1.0d;
    private double fillOpacity = 1.0d;
    private String transform = "";

    public BaseShape() {

    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    public Element getElement(TileContext context) {
        Element e = _getElement(context);
        e.setAttributeNS(null, "fill", fill);
        e.setAttributeNS(null, "stroke", stroke);
        e.setAttributeNS(null, "stroke-width", string(strokeWidth));
        e.setAttributeNS(null, "fill-opacity", string(fillOpacity));
        e.setAttributeNS(null, "transform", transform);
        return e;
    }

    protected String string(double d) {
        return "" + d;
    }

    protected abstract Element _getElement(TileContext context);

}
