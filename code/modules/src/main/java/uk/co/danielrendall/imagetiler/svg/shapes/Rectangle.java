package uk.co.danielrendall.imagetiler.svg.shapes;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.TileContext;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 10:53:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rectangle extends BaseShape {
    private double x = 0.0d;
    private double y = 0.0d;
    private double width = 1.0d;
    private double height = 1.0d;

    public Rectangle() {
        super();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    protected Element _getElement(TileContext context) {
        if (width < 0.0d) {
            width = Math.abs(width);
            x = x - width;
        }
        if (height < 0.0d) {
            height = Math.abs(height);
            y = y - height;
        }
        Element e = context.createElement("rect");
        e.setAttributeNS(null, "x", string(x));
        e.setAttributeNS(null, "y", string(y));
        e.setAttributeNS(null, "width", string(width));
        e.setAttributeNS(null, "height", string(height));
        return e;
    }

}
