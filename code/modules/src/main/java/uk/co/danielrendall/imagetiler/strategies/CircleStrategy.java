package uk.co.danielrendall.imagetiler.strategies;

import org.apache.log4j.Logger;
import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.PixelFilter;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.mathlib.geom2d.Point;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 11:29:51
 * To change this template use File | Settings | File Templates.
 */
public class CircleStrategy extends ScannerStrategy {
    public final static Logger log = Logger.getLogger(CircleStrategy.class);

    private final Iterator<Pixel> pixelIterator;
    
    public CircleStrategy(int xMin, int width, int yMin, int height, PixelFilter filter) {
        super(xMin, width, yMin, height, filter);
        Point center = new Point(((double) xMin) + ((double)width / 2.0d), ((double) yMin) + ((double)height / 2.0d));
        SortedSet<Pixel> pixels = new TreeSet<Pixel>(getRadiusComparator(center));
        GridStrategy strategy = new GridStrategy(xMin, width, yMin, height, filter);
        while (strategy.hasNext()) {
            Pixel next = strategy.next();
            if (filter.shouldInclude(next)) pixels.add(next);
        }
        log.info("Max pixels " + width * height + " pixels, I have " + pixels.size());
        pixelIterator = pixels.iterator();
    }

    public boolean hasNext() {
        return pixelIterator.hasNext();
    }

    public Pixel next() {
        return pixelIterator.next();
    }

    protected Comparator<Pixel> getRadiusComparator(Point center) {
        return new RadiusComparator(center);
    }

    protected static class RadiusComparator implements Comparator<Pixel> {
        private final Point center;
        private final Map<Pixel, Double> distanceCache;
        private final Map<Pixel, Double> angleCache;

        private RadiusComparator(Point center) {
            this.center = center;
            distanceCache = new HashMap<Pixel, Double>();
            angleCache = new HashMap<Pixel, Double>();
        }

        public int compare(Pixel p1, Pixel p2) {
            int ret = Double.compare(distanceToCentre(p2), distanceToCentre(p1));
            if (ret != 0) return ret;
            return Double.compare(angleOfPoint(p1), angleOfPoint(p2));
        }

        private double distanceToCentre(Pixel p) {
            Double d = distanceCache.get(p);
            if (d != null) return d;
            // need to compare using middle of square
            Point pixelCenter = new Point(0.5d + (double) p.getX(), 0.5d + (double) p.getY());
            double distance = pixelCenter.line(center).length();
            distanceCache.put(p, distance);
            return distance;
        }
        
        private double angleOfPoint(Pixel p) {
            Double d = angleCache.get(p);
            if (d != null) return d;
            // need to compare using middle of square
            Point pixelCenter = new Point(0.5d + (double) p.getX(), 0.5d + (double) p.getY());

            double angle = center.line(pixelCenter).getVec().angle();
            angleCache.put(p, angle);
            return angle;
        }
    }
}
