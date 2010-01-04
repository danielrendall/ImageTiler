package uk.co.danielrendall.imagetiler.image;

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
