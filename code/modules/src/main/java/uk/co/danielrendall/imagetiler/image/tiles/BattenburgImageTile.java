package uk.co.danielrendall.imagetiler.image.tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 30, 2006
 * Time: 10:13:09 PM
 */
public class BattenburgImageTile extends SimpleImageTile {
    public void getTile(BufferedImage bi, Color color) {
        Graphics2D graphics = bi.createGraphics();
        graphics.setPaint(lighter(color, 0.5f));
        graphics.fillRect(0,0,WIDTH/2, HEIGHT/2);
        graphics.setPaint(color);
        graphics.fillRect(WIDTH/2,0,WIDTH/2, HEIGHT/2);
        graphics.fillRect(0,HEIGHT/2,WIDTH/2, HEIGHT/2);
        graphics.setPaint(darker(color, 0.5f));
        graphics.fillRect(WIDTH/2,HEIGHT/2,WIDTH/2, HEIGHT/2);
    }
}
