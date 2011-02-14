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

package uk.co.danielrendall.imagetiler.utils;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 17:13:57
 * To change this template use File | Settings | File Templates.
 */
public class SpiralGeneratorTest {

    @Test @Ignore
    public void testSpiralGeneration() throws IOException {
        SpiralGenerator gen = new SpiralGenerator(240, 20.0, 110.0, -Math.PI / 2.0d, 11.0 * Math.PI / 2.0, 200, 0, 360, "/tmp/output.bmp" );
        gen.generateSpiral();
    }
}
