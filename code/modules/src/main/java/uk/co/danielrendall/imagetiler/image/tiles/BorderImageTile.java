package uk.co.danielrendall.imagetiler.image.tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 30, 2006
 * Time: 10:14:38 PM
 */
public class BorderImageTile extends SimpleImageTile {

    protected final static int BORDER = 2;
    protected final static Color BORDERCOLOR = Color.WHITE;


    public void getTile(BufferedImage bi, Color color) {
        Graphics2D graphics = bi.createGraphics();
        graphics.setPaint(BORDERCOLOR);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        graphics.setPaint(color);
        graphics.fillRect(BORDER, BORDER, WIDTH - 2 * BORDER, HEIGHT - 2 * BORDER);
    }
}
