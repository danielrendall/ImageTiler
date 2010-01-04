package uk.co.danielrendall.imagetiler.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel Rendall, Square Root of Minus 1 Ltd.
 * Date: Jul 30, 2006
 * Time: 9:22:36 PM
 */
public interface ImageTile {
    /**
     * Accept command line parameters; details may vary depending on tile
     * @param args
     */
    void initialize(String[] args);

    /**
     * Returns the width - this may be hardcoded or deduced from parameters
     * @return width of this tile
     */
    int getWidth();

    /**
     * Returns the height - this may be hardcoded or deduced from parameters
     * @return height of this tile
     */
    int getHeight();

    /**
     * Create a tile based on the given colour which may then be copied into the
     * master document.
     * @param color The color of the pixel from which this tile is to be made
     */
     void getTile(BufferedImage bi, Color color);
}
