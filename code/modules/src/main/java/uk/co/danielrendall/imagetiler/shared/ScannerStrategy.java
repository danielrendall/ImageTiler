/*
 * Copyright (c) 2009, 2010 Daniel Rendall
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
