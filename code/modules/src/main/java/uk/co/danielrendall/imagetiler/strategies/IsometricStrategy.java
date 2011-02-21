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

import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.PixelFilter;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.mathlib.geom2d.Compass;

/**
 * Strategy which starts with the top-right tile and works in tl -> br diagonal stripes. Intended for
 * use with the Lego tile, so that the overlaps come out right.
 * @author Daniel Rendall
 */
public class IsometricStrategy extends ScannerStrategy {

    private Pixel currentStart;
    private Pixel current;

    private boolean isOnTopRow;

    public IsometricStrategy(int xMin, int width, int yMin, int height, PixelFilter filter) {
        super(xMin, width, yMin, height, filter);
        currentStart = new Pixel(xMax, 0);
        current = currentStart;
        isOnTopRow = true;
    }

    public boolean hasNext() {
        return current != null;
    }

    public Pixel next() {
        Pixel toReturn = current;

        Pixel next = current.displace(Compass.SE);
        // is the next in the grid?
        if (next.getX() <= xMax && next.getY() <= yMax) {
            current = next;
        } else if (isOnTopRow) {
            Pixel nextStart = currentStart.displace(Compass.W);
            if (nextStart.getX() >= xMin) {
                currentStart = nextStart;
                current = currentStart;
            } else {
                isOnTopRow = false;
                nextStart = currentStart.displace(Compass.S);
                if (nextStart.getY() <= yMax) {
                    currentStart = nextStart;
                    current = currentStart;
                } else {
                    // run out of grid...
                    currentStart = null;
                    current = null;
                }
            }
        } else {
            Pixel nextStart = currentStart.displace(Compass.S);
            if (nextStart.getY() <= yMax) {
                currentStart = nextStart;
                current = currentStart;
            } else {
                // run out of grid...
                currentStart = null;
                current = null;
            }
        }
        return toReturn;
    }

}
