package uk.co.danielrendall.imagetiler.svg.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
import uk.co.danielrendall.imagetiler.shared.ConfigStore;
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


    protected static final String NAME_INSET = "inset";
    protected static final String DESCRIPTION_INSET = "Fractional inset";

    protected static final String NAME_STROKE_WIDTH = "strokeWidth";
    protected static final String DESCRIPTION_STROKE_WIDTH = "Width of stroke as fraction of tile size";

    protected static final String NAME_DARK_OPACITY = "darkOpacity";
    protected static final String DESCRIPTION_DARK_OPACITY = "Opacity of dark areas";

    protected static final String NAME_LIGHT_OPACITY = "lightOpacity";
    protected static final String DESCRIPTION_LIGHT_OPACITY = "Opacity of light areas";

    protected final double inset;
    protected final double strokeWidth;
    protected final double darkOpacity;
    protected final double lightOpacity;

    public SimpleSVGTile(
            @DoubleParameter(name = NAME_INSET, description = DESCRIPTION_INSET, defaultValue=0.15d, minValue = 0.0d, maxValue = 0.5d)
            double inset,
            @DoubleParameter(name = NAME_STROKE_WIDTH, description = DESCRIPTION_STROKE_WIDTH, defaultValue=0.05d, minValue = 0.001d, maxValue = 0.5d)
            double strokeWidth,
            @DoubleParameter(name = NAME_DARK_OPACITY, description = DESCRIPTION_DARK_OPACITY, defaultValue=0.8d, minValue = 0.0d, maxValue = 1.0d)
            double darkOpacity,
            @DoubleParameter(name = NAME_LIGHT_OPACITY, description = DESCRIPTION_LIGHT_OPACITY, defaultValue=0.6d, minValue = 0.0d, maxValue = 1.0d)
            double lightOpacity) {
        this.inset = inset;
        this.strokeWidth = strokeWidth;
        this.darkOpacity = darkOpacity;
        this.lightOpacity = lightOpacity;
    }

    //    public SimpleSVGTile(ConfigStore store) {
//        this.inset = store.getDouble("inset");
//        this.strokeWidth = store.getDouble("strokeWidth");
//        this.darkOpacity = store.getDouble("darkOpacity");
//        this.lightOpacity = store.getDouble("lightOpacity");
//    }

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            Rectangle r = new Rectangle();
            double width = context.getWidth();
            double height = context.getHeight();

            r.setX(context.getLeft() + (width * inset));
            r.setY(context.getTop() + (height * inset));
            r.setWidth(width * (1.0d - 2.0d * inset));
            r.setHeight(height * (1.0d - 2.0d * inset));
            r.setFill(hexValue(context.getColor()));
            r.setStroke("black");
            r.setStrokeWidth(width * strokeWidth);
            Element e = r.getElement(context);
            group.appendChild(e);
            return true;
        }
        return false;
    }

    protected String string(double d) {
        // TODO - probably would be better to call Double.toString(d)
        return "" + d;
    }

    protected String hexValue(Color color) {
        // TODO - look at String.format - sure there must be a nicer way of doing this
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
