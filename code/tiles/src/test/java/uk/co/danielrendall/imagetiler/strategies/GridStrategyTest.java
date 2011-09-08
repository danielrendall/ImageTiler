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

import static org.junit.Assert.*;
import org.junit.Test;
import uk.co.danielrendall.imagetiler.shared.NullPixelFilter;
import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.imagetiler.strategies.GridStrategy;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 10:40:37
 * To change this template use File | Settings | File Templates.
 */
public class GridStrategyTest {

    @Test
    public void testSimpleGrid() {
        ScannerStrategy strategy = new GridStrategy();
        strategy.initialise(0, 2, 0, 2, new NullPixelFilter());
        assertEquals(new Pixel(0,0), strategy.next());
        assertEquals(new Pixel(1,0), strategy.next());
        assertEquals(new Pixel(0,1), strategy.next());
        assertEquals(new Pixel(1,1), strategy.next());
        assertFalse(strategy.hasNext());
    }

    @Test
    public void testBiggerGrid() {
        ScannerStrategy strategy = new GridStrategy();
        strategy.initialise(-2, 4, -2, 4, new NullPixelFilter());
        assertEquals(new Pixel(-2,-2), strategy.next());
        assertEquals(new Pixel(-1,-2), strategy.next());
        assertEquals(new Pixel(0,-2), strategy.next());
        assertEquals(new Pixel(1,-2), strategy.next());
        assertEquals(new Pixel(-2,-1), strategy.next());
        assertEquals(new Pixel(-1,-1), strategy.next());
        assertEquals(new Pixel(0,-1), strategy.next());
        assertEquals(new Pixel(1,-1), strategy.next());
        assertEquals(new Pixel(-2,0), strategy.next());
        assertEquals(new Pixel(-1,0), strategy.next());
        assertEquals(new Pixel(0,0), strategy.next());
        assertEquals(new Pixel(1,0), strategy.next());
        assertEquals(new Pixel(-2,1), strategy.next());
        assertEquals(new Pixel(-1,1), strategy.next());
        assertEquals(new Pixel(0,1), strategy.next());
        assertEquals(new Pixel(1,1), strategy.next());
        assertFalse(strategy.hasNext());
    }
}
