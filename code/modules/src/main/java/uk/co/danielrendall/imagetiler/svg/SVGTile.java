package uk.co.danielrendall.imagetiler.svg;

import org.w3c.dom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 9:36:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SVGTile {
    /**
     * Accept command line parameters; details may vary depending on tile
     * @param args
     */
    void initialize(String[] args);

    /**
     *
     * @param group Element to which tile appends children
     * @param context Information about the current location in the image
     */

    boolean getTile(Element group, TileContext context);

}
