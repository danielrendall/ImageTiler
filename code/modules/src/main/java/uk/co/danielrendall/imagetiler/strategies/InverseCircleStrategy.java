package uk.co.danielrendall.imagetiler.strategies;

import org.apache.commons.collections.comparators.ReverseComparator;
import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.PixelFilter;
import uk.co.danielrendall.mathlib.geom2d.Point;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Mar-2010
 * Time: 11:02:03
 * To change this template use File | Settings | File Templates.
 */
public class InverseCircleStrategy extends CircleStrategy {
    public InverseCircleStrategy(int xMin, int width, int yMin, int height, PixelFilter filter) {
        super(xMin, width, yMin, height, filter);
    }

    @Override
    protected Comparator<Pixel> getRadiusComparator(Point center) {
        Comparator<Pixel> basic = super.getRadiusComparator(center);
        return new ReverseComparator(basic);
    }
}
