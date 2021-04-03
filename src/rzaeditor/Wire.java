package rzaeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Intersectionf;

public class Wire {

    Vector2i start = new Vector2i();
    Vector2i end = new Vector2i();
    WireIntersection endWI = null;
    WireIntersection startWI = null;
    Color color = Color.BLACK;
    boolean deleted = false;
    boolean selected = false;

    public Wire() {
    }

    public static Wire createDrawWire() {
        return new Wire();
    }

    public static Wire create(Vector2i s, Vector2i e) {
        Wire w = new Wire();
        w.start = s;
        w.end = e;
        int temp = 0;
        if (w.start.x > w.end.x) {
            temp = w.start.x;
            w.start.x = w.end.x;
            w.end.x = temp;
        }
        if (w.start.y > w.end.y) {
            temp = w.start.y;
            w.start.y = w.end.y;
            w.end.y = temp;
        }
        w.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        w.checkIntersections();
        return w;
    }

    public void delete() {
        Logic.pageWireIntersections.stream().forEach((t) -> {
            t.wireIntersects.remove(this);
            t.checkIsEmpty();
        });
        Logic.pageWires.remove(this);
        deleted = true;
    }

    public void checkIntersections() {
        ArrayList<Wire> wiresTemp = new ArrayList<>(Logic.pageWires);
        for (Wire w : wiresTemp) {
            if (w.equals(this)) {
                continue;
            }
            if (w.deleted) {
                continue;
            }
            if (w.isHorizontal() == isHorizontal()) {
                if (w.start.equals(end)) {
                    if (w.startWI.wireIntersects.size() <= 1) {
                        w.start = start;
                        delete();
                        w.checkIntersections();
                        return;
                    }
                } else if (w.end.equals(start)) {
                    if (w.endWI.wireIntersects.size() <= 1) {
                        w.end = end;
                        delete();
                        w.checkIntersections();
                        return;
                    }
                }
            } else {
                if (w.pointInside(start, 1)) {
                    w.split(start);
                } else if (w.pointInside(end, 1)) {
                    w.split(end);
                }
            }
        }
        if (!deleted) {
            Logic.pageWires.add(this);
            if (startWI != null) {
                startWI.wireIntersects.remove(this);
                startWI.checkIsEmpty();
            }
            if (endWI != null) {
                endWI.wireIntersects.remove(this);
                endWI.checkIsEmpty();
            }
            startWI = WireIntersection.getWI(start);
            startWI.wireIntersects.add(this);
            endWI = WireIntersection.getWI(end);
            endWI.wireIntersects.add(this);
        }
    }
    
    public Vector2i getCenter(){
        return getVec().div(2).add(start);
    }

    public void draw(Graphics2D g) {
        Vector2f t0 = Logic.gridToScreenCenter(start);
        Vector2f t1 = Logic.gridToScreenCenter(end);
        g.setColor(color);
        if(selected){
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(3));
        }
        g.setStroke(new BasicStroke(2));
        Logic.drawLine(g, t0.x, t0.y, t1.x, t1.y);
    }

    public Vector2i getVec() {
        return new Vector2i(end).sub(start);
    }

    public float getLen() {
        return (float) getVec().length();
    }

    public boolean intersectPoint(Vector2i p) {
        Vector2f t = new Vector2f();
        boolean r = Line2D.ptSegDist(start.x, start.y, end.x, end.y, p.x, p.y) == 0;
        return r;
    }

    public boolean isHorizontal() {
        return getVec().y == 0;
    }

    public void connect(Wire w1) {
    }

    public void split(Vector2i p) {
        if (!intersectPoint(p)) {
            throw new IllegalArgumentException("Tried to split at a point not on wire");
        }
        if (p.equals(start) || p.equals(end)) {
            return;
        }
        Vector2i temp = end;
        end = p;
        Wire w1 = Wire.create(p, temp);
    }

    public boolean pointInside(Vector2i p, int offset) {
        if (getLen() < 2) {
            return false;
        }
        Vector2f t = new Vector2f();
        boolean r = false;
        if (isHorizontal()) {
            r = Line2D.ptSegDist(start.x + offset, start.y, end.x - offset, end.y, p.x, p.y) == 0;
        } else {
            r = Line2D.ptSegDist(start.x, start.y + offset, end.x, end.y - offset, p.x, p.y) == 0;
        }
        return r;
    }

    public boolean containsWire(Wire w1) {
        if (getLen() < 2) {
            return false;
        }
        if (w1.getLen() < 1) {
            return false;
        }
        if (isHorizontal() != w1.isHorizontal()) {
            return false;
        }
        if (pointInside(w1.start, 1) || pointInside(w1.end, 1)) {
            return true;
        }
        return false;
    }

    public boolean intersectWire(Wire w1) {
        return intersectWire(w1, null);
    }

    public boolean intersectWire(Wire w1, Vector2i i) {
        Vector2f t = new Vector2f();
        boolean r = Intersectionf.intersectLineLine(start.x, start.y, end.x, end.y, w1.start.x, w1.start.y, w1.end.x, w1.end.y, t);
        if (i != null) {
            i = new Vector2i((int) t.x, (int) t.y);
        }
        return r;
    }

}
