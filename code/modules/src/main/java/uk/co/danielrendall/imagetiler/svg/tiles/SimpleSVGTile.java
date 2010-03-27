package uk.co.danielrendall.imagetiler.svg.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.SVGTile;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.Rectangle;

import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 10:02:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleSVGTile implements SVGTile {

    public void initialize(String[] args) {
        // Nothing to do
    }

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            Rectangle r = new Rectangle();
            double width = context.getWidth();
            double height = context.getHeight();
            r.setX(context.getLeft() + (width * 0.15d));
            r.setY(context.getTop() + (height * 0.15d));
            r.setWidth(width * 0.7d);
            r.setHeight(height * 0.7d);
            r.setFill(hexValue(context.getColor()));
            r.setStroke("black");
            r.setStrokeWidth(width * 0.03d);
            Element e = r.getElement(context);
            group.appendChild(e);
            return true;
        }
        return false;
    }

    protected String string(double d) {
        return "" + d;
    }

    protected String hexValue(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        String rh = Integer.toHexString(r);
        String gh = Integer.toHexString(g);
        String bh = Integer.toHexString(b);
        return "#" + ((r<16) ? "0":"") + rh +
                     ((g<16) ? "0":"") + gh +
                     ((b<16) ? "0":"") + bh;

    }

    protected Color lighter(Color src, float fraction) {
        int r = src.getRed();
        int g = src.getGreen();
        int b = src.getBlue();

        int i = (int)(1.0/(1.0-fraction));
        if ( r == 0 && g == 0 && b == 0) {
           return new Color(i, i, i);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return new Color(Math.min((int)(r/fraction), 255),
                         Math.min((int)(g/fraction), 255),
                         Math.min((int)(b/fraction), 255));
    }

    protected Color darker(Color src, float fraction) {
        return new Color(Math.max((int)(src.getRed()  *fraction), 0),
                 Math.max((int)(src.getGreen()*fraction), 0),
                 Math.max((int)(src.getBlue() *fraction), 0));

    }

}
