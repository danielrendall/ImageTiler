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
import uk.co.danielrendall.imagetiler.tiles.*;
import static junit.framework.Assert.assertEquals;

/**
 * @author Daniel Rendall
 */
public class AnnotationTest {

    private final double Delta = 0.0000001d;

    @Test
    public void testSettingWithAnnotations() {
        SimpleSVGTile tile = new SimpleSVGTile();
        tile.setDarkOpacity(0.4d);
        assertEquals(0.4d, tile.getDarkOpacity(), Delta);
        tile.setDarkOpacity(2.0d);
        assertEquals(1.0d, tile.getDarkOpacity(), Delta);
        tile.setDarkOpacity(-1.0d);
        assertEquals(0.0d, tile.getDarkOpacity(), Delta);
    }

    @Test
    public void testSettingOfChildClassWithInheritedFields() {
        GemSVGTile tile = new GemSVGTile();
        tile.setDarkOpacity(0.4d);
        assertEquals(0.4d, tile.getDarkOpacity(), Delta);
        tile.setDarkOpacity(2.0d);
        assertEquals(1.0d, tile.getDarkOpacity(), Delta);
        tile.setDarkOpacity(-1.0d);
        assertEquals(0.0d, tile.getDarkOpacity(), Delta);
    }

    @Test
    public void testSettingOfChildClassWithInheritedAndNewFields() {
        StarSVGTile tile = new StarSVGTile();
        tile.setDarkOpacity(0.4d);
        assertEquals(0.4d, tile.getDarkOpacity(), Delta);
        tile.setDarkOpacity(2.0d);
        assertEquals(1.0d, tile.getDarkOpacity(), Delta);
        tile.setDarkOpacity(-1.0d);
        assertEquals(0.0d, tile.getDarkOpacity(), Delta);

        tile.setInnerRadius(7.5d);
        assertEquals(7.5d, tile.getInnerRadius(), Delta);
        tile.setInnerRadius(20.0d);
        assertEquals(10.0d, tile.getInnerRadius(), Delta);
        tile.setInnerRadius(0.0d);
        assertEquals(0.001d, tile.getInnerRadius(), Delta);
    }
}
