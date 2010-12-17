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

package uk.co.danielrendall.imagetiler.image.tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 30, 2006
 * Time: 10:19:50 PM
 */
public class CrossImageTile extends SimpleImageTile {
    public void getTile(BufferedImage bi, Color color) {
        Graphics2D graphics = bi.createGraphics();
        graphics.setPaint(color);
        graphics.drawLine(0,0,WIDTH,HEIGHT);
        graphics.drawLine(0,HEIGHT,WIDTH,0);
    }
}
