/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of ImageTiler.
 *
 * ImageTiler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ImageTiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ImageTiler.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.imagetiler.strategies;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import uk.co.danielrendall.imagetiler.annotations.BooleanParameter;
import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
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
@ClassDescriptor(name="Circle", description="Concentric circles running out from the center")
public class CircleStrategy extends ScannerStrategy {
    public final static Logger log = LogManager.getLogger(CircleStrategy.class);

    @BooleanParameter(name = "invert", description = "Make circles run from the outside into the center", defaultValue = false)
    private boolean invert;

    private Iterator<Pixel> pixelIterator;

    public void doAfterInitialise() {
        Point center = new Point(((double) xMin) + ((double)width / 2.0d), ((double) yMin) + ((double)height / 2.0d));
        SortedSet<Pixel> pixels = new TreeSet<Pixel>(getRadiusComparator(center));
        GridStrategy strategy = new GridStrategy();
        strategy.initialise(this);
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
        RadiusComparator radiusComparator = new RadiusComparator(center);
        return invert ? new ReverseComparator(radiusComparator) : radiusComparator;
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
