package uk.co.danielrendall.imagetiler.shared;

import java.awt.*;
import java.awt.image.Raster;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 18:23:18
 * To change this template use File | Settings | File Templates.
 */
public class RasterPixelFilter implements PixelFilter {
    final Raster raster;
    public RasterPixelFilter(Raster raster) {
        this.raster = raster;
    }

    public boolean shouldInclude(Pixel p) {
        int pixel[] = new int[4];
        raster.getPixel(p.getX(),p.getY(),pixel);
        Color color = new Color(pixel[0], pixel[1], pixel[2]);
        return (!Color.WHITE.equals(color));
    }
}
