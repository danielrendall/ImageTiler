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

package uk.co.danielrendall.imagetiler.annotations;

import org.junit.Test;
import uk.co.danielrendall.imagetiler.svg.tiles.GemSVGTile;
import uk.co.danielrendall.imagetiler.svg.tiles.SimpleSVGTile;

import static junit.framework.Assert.assertEquals;

/**
 * @author Daniel Rendall
 */
public class AnnotationTest {

    @Test
    public void testSettingWithAnnotations() {
        SimpleSVGTile tile = new SimpleSVGTile();
//        assertEquals(0.8d, tile.getDarkOpacity(), 0.001d);
        tile.setDarkOpacity(0.4d);
        assertEquals(0.4d, tile.getDarkOpacity(), 0.001d);
        tile.setDarkOpacity(2.0d);
        assertEquals(1.0d, tile.getDarkOpacity(), 0.001d);
        tile.setDarkOpacity(-1.0d);
        assertEquals(0.0d, tile.getDarkOpacity(), 0.001d);
    }

    @Test
    public void testSettingOfChildClassWithAnnotations() {
        GemSVGTile tile = new GemSVGTile();
//        assertEquals(0.8d, tile.getDarkOpacity(), 0.001d);
        tile.setDarkOpacity(0.4d);
        assertEquals(0.4d, tile.getDarkOpacity(), 0.001d);
        tile.setDarkOpacity(2.0d);
        assertEquals(1.0d, tile.getDarkOpacity(), 0.001d);
        tile.setDarkOpacity(-1.0d);
        assertEquals(0.0d, tile.getDarkOpacity(), 0.001d);
    }
}
