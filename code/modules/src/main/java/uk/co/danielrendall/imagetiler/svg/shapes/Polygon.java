package uk.co.danielrendall.imagetiler.svg.shapes;

import org.w3c.dom.Element;
import uk.co.danielrendall.imagetiler.svg.TileContext;
import uk.co.danielrendall.mathlib.geom2d.Point;

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

    private List<Point> points;

    public Polygon() {
        super();
        clear();
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public void clear() {
        points = new ArrayList<Point>();
    }

    // mutates state - maybe bad?
    public void rotate(Point center, double angle) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            points.set(i, center.displace(center.line(point).getVec().rotate(angle)));
        }
    }


    protected Element _getElement(TileContext context) {
        Element e = context.createElement("path");
        boolean isFirst = true;
        StringBuffer path = new StringBuffer();
        for (Point point : points) {
            path.append(isFirst ? "M " : "L ");
            isFirst = false;
            path.append(point.x()).append(" ").append(point.y()).append(" ");
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
