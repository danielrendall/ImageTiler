package uk.co.danielrendall.imagetiler.image.tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 30, 2006
 * Time: 10:25:09 PM
 */
public class GemImageTile extends SimpleImageTile {

    public void getTile(BufferedImage bi, Color color) {
        Graphics2D graphics = bi.createGraphics();

        Polygon poly = new Polygon();
        poly.addPoint(WIDTH/2,0);
        poly.addPoint(WIDTH-1,HEIGHT/2);
        poly.addPoint(WIDTH/2,HEIGHT-1);
        poly.addPoint(0,HEIGHT/2);

        graphics.setClip(poly);

        graphics.setPaint(color);
        graphics.fillRect(0,0,WIDTH/2, HEIGHT/2);
        graphics.setPaint(lighter(color, 0.8f));
        graphics.fillRect(WIDTH/2,0,WIDTH/2, HEIGHT/2);
        graphics.fillRect(0,HEIGHT/2,WIDTH/2, HEIGHT/2);
        graphics.setPaint(lighter(color, 0.6f));
        graphics.fillRect(WIDTH/2,HEIGHT/2,WIDTH/2, HEIGHT/2);

    }
}
