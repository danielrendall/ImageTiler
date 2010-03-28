package uk.co.danielrendall.imagetiler.shared;

import uk.co.danielrendall.imagetiler.shared.Pixel;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 10:27:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class ScannerStrategy implements Iterator<Pixel> {

    protected final int xMin;
    protected final int width;
    protected final int yMin;
    protected final int height;
    protected final PixelFilter filter;

    protected final int xMax;
    protected final int yMax;

    protected ScannerStrategy(int xMin, int width, int yMin, int height, PixelFilter filter) {
        this.xMin = xMin;
        this.width = width;
        this.yMin = yMin;
        this.height = height;
        this.filter = filter;

        this.xMax = xMin + width - 1;
        this.yMax = yMin + height - 1;
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
