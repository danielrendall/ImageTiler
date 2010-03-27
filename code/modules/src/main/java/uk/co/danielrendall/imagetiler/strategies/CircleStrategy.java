package uk.co.danielrendall.imagetiler.strategies;

import org.apache.log4j.Logger;
import uk.co.danielrendall.imagetiler.Pixel;
import uk.co.danielrendall.imagetiler.PixelFilter;
import uk.co.danielrendall.imagetiler.ScannerStrategy;

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
        double xCenter = ((double) xMin) + ((double)width / 2.0d);
        double yCenter = ((double) yMin) + ((double)height / 2.0d);
        SortedSet<Pixel> pixels = new TreeSet<Pixel>(new RadiusComparator(xCenter, yCenter));
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

    private static class RadiusComparator implements Comparator<Pixel> {
        private final double xCenter;
        private final double yCenter;
        private final Map<Pixel, Double> distanceCache;
        private final Map<Pixel, Double> angleCache;

        private RadiusComparator(double xCenter, double yCenter) {
            this.xCenter = xCenter;
            this.yCenter = yCenter;
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
            double px = 0.5d + (double) p.getX();
            double py = 0.5d + (double) p.getY();
            double dx = px - xCenter;
            double dy = py - yCenter;
            double distance = Math.sqrt(dx * dx + dy * dy);
            distanceCache.put(p, distance);
            return distance;
        }
        
        private double angleOfPoint(Pixel p) {
            Double d = angleCache.get(p);
            if (d != null) return d;
            // need to compare using middle of square
            double px = 0.5d + (double) p.getX();
            double py = 0.5d + (double) p.getY();
            double dx = px - xCenter;
            double angle = py - yCenter;
            double distance = Math.atan2(angle, dx);
            angleCache.put(p, distance);
            return distance;
        }
    }
}
