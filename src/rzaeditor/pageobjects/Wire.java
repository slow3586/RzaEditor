package rzaeditor.pageobjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import jdk.nashorn.internal.objects.NativeError;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Intersectionf;
import org.joml.primitives.Rectanglei;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public class Wire extends PageObjectBase{
    WireIntersection endWI = null;
    WireIntersection startWI = null;
    Color color = Color.BLACK;
    
    Wire(Vector2i p){
        super(p);
    }
    
    @Override
    public String getType() {
        return "Провод";
    }
    
    public static void checkAllWires(){
        
        for (Wire w : Page.current.getWires()) {
            for (WireIntersection wi : Page.current.getWireIntersections()) {
                if(!wi.wires.contains(w) && w.pointInside(wi.pos, 1)){
                    w.split(wi.pos);
                    return;
                }
            }
        }
        
        int index = 0;
        ArrayList<Wire> wires = new ArrayList<>();
        wires.addAll(Page.current.getWires());
        for (int i = 0; i < wires.size()-1; i++) {
            Wire w0 = wires.get(i);
            for (int j = i+1; j < wires.size(); j++) {
                Wire w1 = wires.get(j);  
                
                if(w0.isHorizontal() == w1.isHorizontal() && w0.startWI.pos == w1.endWI.pos && w0.startWI.wires.size()==2){
                    w0.setStartWI(w1.startWI);
                    w1.delete();
                    return;
                }
                if(w0.isHorizontal() == w1.isHorizontal() && w0.endWI.pos == w1.startWI.pos && w0.endWI.wires.size()==2){
                    w0.setEndWI(w1.endWI);
                    w1.delete();
                    return;
                }
                
                if(w0.isHorizontal() == w1.isHorizontal() && w0.containsWire(w1)){
                    if(w0.isHorizontal()){
                        if(w0.pos.x > w1.pos.x){
                            w0.setStartWI(w1.startWI);
                        }else{
                            w0.setEndWI(w1.startWI);
                        }
                    }else{
                        if(w0.pos.y > w1.pos.y){
                            w0.setStartWI(w1.startWI);
                        }else{
                            w0.setEndWI(w1.startWI);
                        }
                    }
                    w1.delete();
                    return;
                }

                if(w0.pointInside(w1.startWI.pos, 1)){
                    w0.split(w0.startWI.pos);
                    return;
                }
                if(w0.pointInside(w1.endWI.pos, 1)){
                    w0.split(w1.endWI.pos);
                    return;
                }
                if(w1.pointInside(w0.startWI.pos, 1)){
                    w1.split(w0.startWI.pos);
                    return;
                }
                if(w1.pointInside(w0.endWI.pos, 1)){
                    w1.split(w0.endWI.pos);
                    return;
                }
            }
        }
        
        
        
    }
    
    public void split(Vector2i p){
        Vector2i t = new Vector2i(endWI.pos);
        setEndWI(WireIntersection.getWI(p));
        Wire.create(p, t, true);
    }
    
    public void setStartWI(WireIntersection i){
        if(startWI!=null)
            startWI.removeWire(this);
        setStartEndWI(i.pos, endWI.pos);
    }
    
    public void setEndWI(WireIntersection i){
        if(endWI!=null)
            endWI.removeWire(this);
        setStartEndWI(startWI.pos, i.pos);
    }
    
    public boolean setStartEndWI(Vector2i s0, Vector2i e0){
        Vector2i s = new Vector2i(s0);
        Vector2i e = new Vector2i(e0);
        Logic.fixVectorPositions(s, e);
        startWI = WireIntersection.getWI(s);
        endWI = WireIntersection.getWI(e);
        pos = startWI.pos;
        startWI.wires.add(this);
        endWI.wires.add(this);
        if(startWI.wireless.contains(endWI)){
            delete();
            return false;
        }
        return true;
    }
    
    public static Wire create(Vector2i s, Vector2i e, boolean update) {
        if(s.x!=e.x && s.y!=e.y){
            throw new IllegalArgumentException("Tried to create a non-straight wire. s:"+s.x+" "+s.y+" e:"+e.x+" "+e.y);
        }
        
        Wire w = new Wire(s);
        if(!w.setStartEndWI(s, e)) return null;
        w.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        Page.current.objects.add(w);
        
        checkAllWires();
        
        return w;
    }

    @Override
    public Vector2i getSize() {
        return getVec();
    }

    public void delete() {
        startWI.wires.remove(this);
        startWI.checkIsEmpty();
        startWI=null;
        endWI.wires.remove(this);
        endWI.checkIsEmpty();
        endWI=null;
        Page.current.objects.remove(this);
        checkAllWires();
    }
    
    public static boolean canBePlacedAt(Vector2i s, Vector2i e){
        return true;
    }
    
    public Vector2i getCenter(){
        return getVec().div(2).add(startWI.pos);
    }

    public void draw() {
        Drawing.setTranslateGrid(startWI.pos);
        Drawing.setColor(Color.black);
        //Drawing.setColor(color);
        selectedCheck();
        Drawing.setStroke(1 * Logic.zoom);
        Vector2i s = getVec();
        Drawing.drawLineGrid(0,0, s.x, s.y);
    }

    public Vector2i getVec() {
        return new Vector2i(endWI.pos).sub(startWI.pos);
    }

    public float getLen() {
        return (float) getVec().length();
    }
    
    public static boolean isHorizontal(Vector2i s, Vector2i e) {
        return new Vector2i(e).sub(s).y==0;
    }

    public boolean isHorizontal() {
        return getVec().y == 0;
    }

    public boolean pointInside(Vector2i p, int offset) {
        if (getLen() < 2) {
            return false;
        }
        Vector2f t = new Vector2f();
        boolean r = false;
        if (isHorizontal()) {
            r = Line2D.ptSegDist(startWI.pos.x + offset, startWI.pos.y, endWI.pos.x - offset, endWI.pos.y, p.x, p.y) == 0;
        } else {
            r = Line2D.ptSegDist(startWI.pos.x, startWI.pos.y + offset, endWI.pos.x, endWI.pos.y - offset, p.x, p.y) == 0;
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
        if (pointInside(w1.startWI.pos, 1) || pointInside(w1.endWI.pos, 1)) {
            return true;
        }
        return false;
    }

    
    public boolean intersectWire(Wire w1) {
        return intersectWire(w1, null);
    }

    public boolean intersectWire(Wire w1, Vector2i at) {
        Vector2f t = new Vector2f();
        //new Rectanglei(startWI.pos.x, startWI.pos.y, endWI.pos.x, endWI.pos.y).intersectsRectangle(new Rectanglei(w1.startWI.pos.x, w1.startWI.pos.y, w1.endWI.pos.x, w1.endWI.pos.y));
        boolean r = Intersectionf.intersectLineLine(startWI.pos.x, startWI.pos.y, endWI.pos.x, endWI.pos.y, w1.startWI.pos.x, w1.startWI.pos.y, w1.endWI.pos.x, w1.endWI.pos.y, t);
         if (at != null) {
            at.x = (int) t.x;
            at.y = (int) t.y;
        }
        return r;
    }
    
    public boolean intersectLine(Vector2i s, Vector2i e, Vector2i at) {
        Vector2f t = new Vector2f();
        boolean r = Intersectionf.intersectLineLine(s.x, s.y, e.x, e.y, startWI.pos.x, startWI.pos.y, endWI.pos.x, endWI.pos.y, t);
        if (at != null) {
            at.x = (int) t.x;
            at.y = (int) t.y;
        }
        return r;
    }
}
