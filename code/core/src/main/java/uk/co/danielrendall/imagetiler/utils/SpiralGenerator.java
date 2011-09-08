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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 17:14:16
 * To change this template use File | Settings | File Templates.
 */
public class SpiralGenerator {

    private final int imageSize;
    private final double startRadius;
    private final double startAngle;
    private final int numberOfPoints;
    private final double radiusIncrement;
    private final double angleIncrement;
    private final double startHue;
    private final String outputFilename;
    private final double hueIncrement;


    public SpiralGenerator(int imageSize, double startRadius, double endRadius, double startAngle, double endAngle, int numberOfPoints, int startHue, int endHue, String outputFilename) {
        this.imageSize = imageSize;
        this.startRadius = startRadius;
        this.startAngle = startAngle;
        this.numberOfPoints = (numberOfPoints > 1) ? numberOfPoints : 2;
        this.startHue = (double)startHue;
        double endHue1 = (double) endHue;
        this.outputFilename = outputFilename;
        this.radiusIncrement = (endRadius - startRadius) / (double)(numberOfPoints - 1);
        this.angleIncrement = (endAngle - startAngle) / (double)(numberOfPoints - 1);
        this.hueIncrement = (endHue1 - this.startHue) / (double)(numberOfPoints - 1);

    }

    public void generateSpiral() throws IOException {
        BufferedImage biOut = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics =biOut.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0,biOut.getWidth(), biOut.getHeight());

        WritableRaster wr = biOut.getRaster();
        double centre = (double)imageSize / 2.0d;

        for (int i=0; i<= numberOfPoints; i++) {
            double radius = startRadius + (radiusIncrement * (double)i);
            double angle = startAngle + (angleIncrement * (double)i);
            double hue = startHue + (hueIncrement * (double)i);
            double xPos = centre + (radius * Math.cos(angle));
            double yPos = centre + (radius * Math.sin(angle));
            int x = (int)Math.round(xPos);
            int y = (int)Math.round(yPos);
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x >= imageSize) x = imageSize - 1;
            if (y >= imageSize) y = imageSize - 1;
            int color = getColorForHue(hue);
            int r = (color & 0xFF0000) >> 16;
            int g = (color & 0xFF00) >> 8;
            int b = (color & 0xFF);

            wr.setPixel(x, y, new int[] {r, g, b});
        }

        biOut.setData(wr);
        ImageIO.write(biOut, "bmp", new File(outputFilename));

    }

    private int getColorForHue(double hue) {
        return Color.HSBtoRGB((float)(hue / 360.0d), 1.0f, 1.0f);
    }
}
