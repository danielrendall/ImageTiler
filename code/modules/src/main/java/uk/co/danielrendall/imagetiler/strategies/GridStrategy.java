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

import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
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
@ClassDescriptor(name="Grid", description="Row then column from top-left to bottom-right")
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
