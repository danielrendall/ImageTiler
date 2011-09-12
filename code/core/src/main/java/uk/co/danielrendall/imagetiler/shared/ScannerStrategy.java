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

package uk.co.danielrendall.imagetiler.shared;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 10:27:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class ScannerStrategy implements Iterator<Pixel> {

    protected int xMin;
    protected int width;
    protected int yMin;
    protected int height;
    protected PixelFilter filter;

    protected int xMax;
    protected int yMax;

    public void initialise(ScannerStrategy another) {
        initialise(another.xMin, another.width, another.yMin, another.height, another.filter);
    }

    public void initialise(int xMin, int width, int yMin, int height, PixelFilter filter) {
        this.xMin = xMin;
        this.width = width;
        this.yMin = yMin;
        this.height = height;
        this.filter = filter;

        this.xMax = xMin + width - 1;
        this.yMax = yMin + height - 1;
        doAfterInitialise();
    }

    public abstract void doAfterInitialise();

    public final void remove() {
        throw new UnsupportedOperationException();
    }

    public static class NullImplementation extends ScannerStrategy {
        @Override
        public void doAfterInitialise() {

        }

        public boolean hasNext() {
            return false;
        }

        public Pixel next() {
            return null; 
        }
    }
}
