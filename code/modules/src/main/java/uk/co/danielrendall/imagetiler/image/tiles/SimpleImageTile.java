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

import uk.co.danielrendall.imagetiler.image.ImageTile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 30, 2006
 * Time: 9:27:27 PM
 */
public class SimpleImageTile implements ImageTile {

    protected final static int WIDTH = 12;
    protected final static int HEIGHT = 12;

    public void initialize(String[] args) {
        // nothing to do
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public void getTile(BufferedImage bi, Color color) {
        Graphics2D graphics = bi.createGraphics();
        graphics.setPaint(color);
        graphics.fillRect(0,0,WIDTH, HEIGHT);
    }

    protected Color lighter(Color src, float fraction) {
        int r = src.getRed();
        int g = src.getGreen();
        int b = src.getBlue();

        int i = (int)(1.0/(1.0-fraction));
        if ( r == 0 && g == 0 && b == 0) {
           return new Color(i, i, i);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return new Color(Math.min((int)(r/fraction), 255),
                         Math.min((int)(g/fraction), 255),
                         Math.min((int)(b/fraction), 255));
    }

    protected Color darker(Color src, float fraction) {
        return new Color(Math.max((int)(src.getRed()  *fraction), 0),
                 Math.max((int)(src.getGreen()*fraction), 0),
                 Math.max((int)(src.getBlue() *fraction), 0));

    }
}
