package uk.co.danielrendall.imagetiler.svg.tiles;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.imagetiler.svg.shapes.*;

import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 12:38:16
 * To change this template use File | Settings | File Templates.
 */
public class SquareExtrudedSVGTile extends SimpleSVGTile {

    public boolean getTile(Element group, TileContext context) {

        if (!context.getColor().equals(Color.WHITE)) {
            double width = context.getWidth();
            double height = context.getHeight();
            Polygon p = new Polygon();
            p.setFill("white");
            p.setStroke("black");
            p.setStrokeWidth(width * 0.03d);

            double effectiveLeft = context.getLeft() + (width * 0.15d);
            double effectiveTop = context.getTop() + (height * 0.15d);
            double effectiveBottom = context.getBottom() - (height * 0.15d);
            double effectiveRight = context.getRight() - (width * 0.15d);

            switch (context.getQuadrant()) {

                case NE:
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveLeft, effectiveTop);
                    p.addPoint(effectiveLeft, effectiveBottom);
                    group.appendChild(p.getElement(context));
                    p.clear();
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveRight, effectiveBottom);
                    p.addPoint(effectiveLeft, effectiveBottom);
                    group.appendChild(p.getElement(context));
                    break;
                case N:
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveRight, effectiveBottom);
                    p.addPoint(effectiveLeft, effectiveBottom);
                    group.appendChild(p.getElement(context));
                    break;
                case NW:
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveRight, effectiveTop);
                    p.addPoint(effectiveRight, effectiveBottom);
                    group.appendChild(p.getElement(context));
                    p.clear();
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveLeft, effectiveBottom);
                    p.addPoint(effectiveRight, effectiveBottom);
                    group.appendChild(p.getElement(context));
                    break;
                case W:
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveRight, effectiveTop);
                    p.addPoint(effectiveRight, effectiveBottom);
                    group.appendChild(p.getElement(context));
                    break;
                case SW:
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveRight, effectiveBottom);
                    p.addPoint(effectiveRight, effectiveTop);
                    group.appendChild(p.getElement(context));
                    p.clear();
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveLeft, effectiveTop);
                    p.addPoint(effectiveRight, effectiveTop);
                    group.appendChild(p.getElement(context));
                    break;
                case S:
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveLeft, effectiveTop);
                    p.addPoint(effectiveRight, effectiveTop);
                    group.appendChild(p.getElement(context));
                    break;
                case SE:
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveLeft, effectiveBottom);
                    p.addPoint(effectiveLeft, effectiveTop);
                    group.appendChild(p.getElement(context));
                    p.clear();
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveRight, effectiveTop);
                    p.addPoint(effectiveLeft, effectiveTop);
                    group.appendChild(p.getElement(context));
                    break;
                case E:
                    p.addPoint(0.0d, 0.0d);
                    p.addPoint(effectiveLeft, effectiveBottom);
                    p.addPoint(effectiveLeft, effectiveTop);
                    group.appendChild(p.getElement(context));
                    break;
                default:
                    break;
            }

            Rectangle r = new Rectangle();
            r.setX(effectiveLeft);
            r.setY(effectiveTop);
            r.setWidth(effectiveRight - effectiveLeft);
            r.setHeight(effectiveBottom - effectiveTop);
            r.setFill(hexValue(context.getColor()));
            r.setStroke("black");
            r.setStrokeWidth(width * 0.03d);
            Element e = r.getElement(context);
            group.appendChild(e);

            return true;
        }
        return false;
    }

}
