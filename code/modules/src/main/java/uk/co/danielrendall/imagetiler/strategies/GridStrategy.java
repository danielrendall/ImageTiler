package uk.co.danielrendall.imagetiler.strategies;

import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.PixelFilter;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 10:34:25
 * To change this template use File | Settings | File Templates.
 */
public class GridStrategy extends ScannerStrategy {

    private int x, y;
    private boolean hasNext;

    public GridStrategy(int xMin, int width, int yMin, int height, PixelFilter filter) {
        super(xMin, width, yMin, height, filter);
        x = xMin;
        y = yMin;
        hasNext = true;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public Pixel next() {
        Pixel next = new Pixel(x,y);
        x+=1;
        if (x > xMax) {
            x = xMin;
            y+=1;
        }
        if (y > yMax) {
            hasNext = false;
        }
        return next;
    }

}
