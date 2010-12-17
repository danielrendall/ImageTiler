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

import org.junit.Test;
import uk.co.danielrendall.imagetiler.strategies.GridStrategy;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Mar-2010
 * Time: 10:17:13
 * To change this template use File | Settings | File Templates.
 */
public class ConfigStoreTest {

    @Test
    public void testStore() {
        ConfigStore store = new ConfigStore("alpha=2.3, beta = 67, GAMMA = 9.7,dElTa=2");
        assertEquals(2.3d, store.getDouble("alpha"), 0.0001d);
        assertEquals(2.3d, store.getDouble("ALPHA"), 0.0001d);
        assertEquals(9.7d, store.getDouble("gamma"), 0.0001d);
        assertEquals(new Integer(2), store.getInt("delta"));
        assertEquals(new Integer(67), store.getInt("BETA"));

        // conversion to double
        assertEquals(67.0d, store.getDouble("beta"), 0.0001d);

        assertNull(store.getDouble("Epsilon"));
        assertNull(store.getInt("ZETA"));
    }

}
