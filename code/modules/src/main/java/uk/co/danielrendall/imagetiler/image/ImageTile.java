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

package uk.co.danielrendall.imagetiler.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 30, 2006
 * Time: 9:22:36 PM
 */
public interface ImageTile {
    /**
     * Accept command line parameters; details may vary depending on tile
     * @param args
     */
    void initialize(String[] args);

    /**
     * Returns the width - this may be hardcoded or deduced from parameters
     * @return width of this tile
     */
    int getWidth();

    /**
     * Returns the height - this may be hardcoded or deduced from parameters
     * @return height of this tile
     */
    int getHeight();

    /**
     * Create a tile based on the given colour which may then be copied into the
     * master document.
     * @param color The color of the pixel from which this tile is to be made
     */
     void getTile(BufferedImage bi, Color color);
}
