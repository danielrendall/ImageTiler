package uk.co.danielrendall.imagetiler.svg.shapes;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.TileContext;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Aug 1, 2006
 * Time: 10:53:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Polygon extends BaseShape {

    private List<Point2D> points;

    public Polygon() {
        super();
        clear();
    }

    public void addPoint(double x, double y) {
        points.add(new Point2D.Double(x, y));
    }

    public void clear() {
        points = new ArrayList<Point2D>();
    }


    protected Element _getElement(TileContext context) {
        Element e = context.createElement("path");
        boolean isFirst = true;
        StringBuffer path = new StringBuffer();
        for (Point2D point : points) {
            path.append(isFirst ? "M " : "L ");
            isFirst = false;
            path.append(point.getX()).append(" ").append(point.getY()).append(" ");
        }
        path.append("z");
        e.setAttributeNS(null, "d", path.toString());
        return e;
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "points=" + points + super.toString() +
                '}';
    }
}
