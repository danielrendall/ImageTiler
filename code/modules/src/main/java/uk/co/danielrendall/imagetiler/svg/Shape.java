package uk.co.danielrendall.imagetiler.svg;

import org.w3c.dom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 11:01:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Shape {
    public Element getElement(TileContext context);
}
