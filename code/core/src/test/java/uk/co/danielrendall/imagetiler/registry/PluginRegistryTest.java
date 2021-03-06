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

package uk.co.danielrendall.imagetiler.registry;

import org.junit.Test;
import uk.co.danielrendall.imagetiler.ITContext;
import uk.co.danielrendall.imagetiler.ImageTilerApplication;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author Daniel Rendall
 */
public class PluginRegistryTest {

    @Test
    public void testPluginRegistry() throws InstantiationException, IllegalAccessException {
        PluginRegistry registry = ImageTilerApplication.createPluginRegistry();
//        assertEquals(GemSVGTile.class, registry.getNewInstance(ImageTilerApplication.PLUGIN_TYPE_TILE, "Gem").getClass());
//        assertEquals(CircleStrategy.class, registry.getNewInstance(ImageTilerApplication.PLUGIN_TYPE_STRATEGY, "Circle").getClass());
        assertNull(registry.getNewInstance(ITContext.PLUGIN_TYPE_STRATEGY, "Oval"));
        assertNull(registry.getNewInstance("NoSuchType", "Thing"));
    }
}
