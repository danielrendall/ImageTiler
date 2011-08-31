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

import org.apache.commons.collections.comparators.ReverseComparator;
import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.PixelFilter;
import uk.co.danielrendall.mathlib.geom2d.Point;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Mar-2010
 * Time: 11:02:03
 * To change this template use File | Settings | File Templates.
 */
@ClassDescriptor(name="Inverse Circle", description="Concentric circles coming in from the outside")
public class InverseCircleStrategy extends CircleStrategy {
    public InverseCircleStrategy(int xMin, int width, int yMin, int height, PixelFilter filter) {
        super(xMin, width, yMin, height, filter);
    }

    @Override
    protected Comparator<Pixel> getRadiusComparator(Point center) {
        Comparator<Pixel> basic = super.getRadiusComparator(center);
        return new ReverseComparator(basic);
    }
}
