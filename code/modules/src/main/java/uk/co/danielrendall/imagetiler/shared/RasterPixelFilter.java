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

import java.awt.*;
import java.awt.image.Raster;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 18:23:18
 * To change this template use File | Settings | File Templates.
 */
public class RasterPixelFilter implements PixelFilter {
    final Raster raster;
    public RasterPixelFilter(Raster raster) {
        this.raster = raster;
    }

    public boolean shouldInclude(Pixel p) {
        int pixel[] = new int[4];
        raster.getPixel(p.getX(),p.getY(),pixel);
        Color color = new Color(pixel[0], pixel[1], pixel[2]);
        return (!Color.WHITE.equals(color));
    }
}
