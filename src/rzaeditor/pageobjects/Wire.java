package rzaeditor.pageobjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Intersectionf;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public class Wire extends PageObjectBase{
    
    WireIntersection endWI = null;
    WireIntersection startWI = null;
    boolean deleted = false;
    
    Vector2i start = new Vector2i();
    Vector2i end = new Vector2i();
    Color color = Color.BLACK;
    
    Wire(Vector2i p){
        super(p);
    }
    
    public static Wire create(Vector2i s, Vector2i e, boolean update) {
        if(s.x!=e.x && s.y!=e.y){
            throw new IllegalArgumentException("Tried to create a non-straight wire. s:"+s.x+" "+s.y+" e:"+e.x+" "+e.y);
        }
        
        Wire w = new Wire(s);
        w.start = new Vector2i(s);
        w.end = new Vector2i(e);
        Logic.fixVectorPositions(w.start, w.end);
        w.setSize(new Vector2i(w.end).sub(w.start));
        w.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        
        if(update)
            w.updatePageInteractions();
        return w;
    }

    public void delete() {
        new HashSet<>(Page.current.wireIntersections).stream().forEach((t) -> {
            t.wireIntersects.remove(this);
            t.checkIsEmpty();
        });
        Page.current.wires.remove(this);
        deleted = true;
    }
    
    public static boolean canBePlacedAt(Vector2i s, Vector2i e){
        Vector2i vec = new Vector2i(e).sub(s);
        
        if(vec.length()<=0) return false;
        
        for (Wire w1 : Page.current.wires) {
            if ((vec.y == 0)==w1.isHorizontal() && (w1.pointInside(s, 1) || w1.pointInside(e, 1))) {
                return false;
            }
        }
        
        return Logic.isInsideGrid(s) && Logic.isInsideGrid(e);
    }

    public void updatePageInteractions() {
        Page.current.wires.add(this);
        
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
        
        for (Wire w : new ArrayList<>(Page.current.wires)) {
            if (w.equals(this) || w.deleted)
                continue;
            if (w.isHorizontal() == isHorizontal()) {
                //if(!allowMerge) continue;
                if (w.start.equals(end)) {
                    if (w.startWI.wireIntersects.size() <= 2) {
                        w.start = start;
                        delete();
                        w.updatePageInteractions();
                        return;
                    }
                } else if (w.end.equals(start)) {
                    if (w.endWI.wireIntersects.size() <= 2) {
                        w.end = end;
                        delete();
                        w.updatePageInteractions();
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
        
        for (WireIntersection wi : new HashSet<>(Page.current.wireIntersections)) {
            if(pointInside(wi.pos, 1)){
                split(wi.pos);
            }
        }
        
    }
    
    public Vector2i getCenter(){
        return getVec().div(2).add(start);
    }

    public void draw() {
        Drawing.setColor(color);
        if(selected){
            Drawing.setColor(Color.RED);
            Drawing.setStroke(3);
        }
        Drawing.setStroke(1 * Logic.zoom);
        Drawing.drawLine(Logic.gridToScreen(start), Logic.gridToScreen(end));
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
        setSize(new Vector2i(end).sub(start));
        updatePageInteractions();
        Wire w1 = Wire.create(p, temp, true);
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
