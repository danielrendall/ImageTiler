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

import org.apache.log4j.Logger;
import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.PixelFilter;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;

import java.util.*;

/**
 * @author Daniel Rendall
 */
@ClassDescriptor(name="Chessboard", description="White chessboard squares first, then black")
public class ChessboardStrategy extends ScannerStrategy {
    public final static Logger log = Logger.getLogger(ChessboardStrategy.class);

    private Iterator<Pixel> pixelIterator;

    public void doAfterInitialise() {
        ScannerStrategy strategy = new CircleStrategy();
        strategy.initialise(this);
        List<Pixel> pixels = new ArrayList<Pixel>();
        PixelFilter white = new ChessboardPixelFilter(filter, true);
        while (strategy.hasNext()) {
            Pixel next = strategy.next();
            if (white.shouldInclude(next)) pixels.add(next);
        }
        strategy = new CircleStrategy();
        strategy.initialise(this);
        PixelFilter black = new ChessboardPixelFilter(filter, false);
        while (strategy.hasNext()) {
            Pixel next = strategy.next();
            if (black.shouldInclude(next)) pixels.add(next);
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

    private class ChessboardPixelFilter implements PixelFilter {

        private final PixelFilter delegate;
        private final boolean isWhite;

        private ChessboardPixelFilter(PixelFilter delegate, boolean white) {
            this.delegate = delegate;
            isWhite = white;
        }

        public boolean shouldInclude(Pixel p) {
            if (!delegate.shouldInclude(p)) return false;
            return (p.getX() + p.getY()) %2 == (isWhite ? 0 : 1);
        }
    }
}
