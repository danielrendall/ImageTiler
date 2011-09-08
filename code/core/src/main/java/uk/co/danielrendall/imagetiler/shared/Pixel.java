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

package uk.co.danielrendall.imagetiler.shared;

import uk.co.danielrendall.mathlib.geom2d.Compass;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 10:28:48
 * To change this template use File | Settings | File Templates.
 */
public class Pixel {

    private final int x;
    private final int y;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pixel(Pixel start, int xDisp, int yDisp) {
        this.x = start.x + xDisp;
        this.y = start.y + yDisp;
    }

    public Pixel displace(int distance, Compass direction) {
        switch (direction) {

            case NE:
                return new Pixel(this, distance, -distance);
            case NW:
                return new Pixel(this, -distance, -distance);
            case SW:
                return new Pixel(this, -distance, distance);
            case SE:
                return new Pixel(this, distance, distance);
            case N:
                return new Pixel(this, 0, -distance);
            case S:
                return new Pixel(this, 0, distance);
            case E:
                return new Pixel(this, distance, 0);
            case W:
                return new Pixel(this, -distance, 0);
            default:
                return new Pixel(this, 0, 0);
        }
    }

    public Pixel displace(Compass direction) {
        return displace(1, direction);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pixel pixel = (Pixel) o;

        if (x != pixel.x) return false;
        if (y != pixel.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
