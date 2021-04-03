package rzaeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Intersectionf;

public class Logic {

    public static Vector2i pageSize = new Vector2i(420, 297);
    public static Vector2i pagePos = new Vector2i(0,0);
    public static int gridGap = 6;
    public static float zoomGridGap = 6;
    public static float zoom = 1.0f;
    public static Vector2i windowSize = new Vector2i(640,480);
    public static MainFrame mf = MainFrame.ins;
    public static EditPanel ep = EditPanel.ins;
    public static boolean isDrawing = false;
    public static Vector2i cursorPosPage = new Vector2i();
    public static Vector2i cursorPosGrid = new Vector2i();
    public static Vector2f cursorPosPageGridSnap = new Vector2f();
    //public static Vector2i dp0 = new Vector2i();
    //public static Vector2i dp1 = new Vector2i();
    public static BufferedImage bi;
    public static DrawMode dm = DrawMode.NONE;
    public static boolean dwGood = false;
    public static HashSet<Wire> pageWires = new HashSet<>();
    public static HashSet<WireIntersection> pageWireIntersections = new HashSet<>();
    public static Wire dw = Wire.createDrawWire();
    public static boolean cursorGridMoved = false;
    
    public static class WireIntersection{
        Vector2i pos = new Vector2i();
        HashSet<Wire> wireIntersects = new HashSet<>();
        
        private WireIntersection(Vector2i p){
            pos = p;
            for (Wire wire : pageWires) {
                if(wire.start.equals(pos)){
                    wire.startWI=this;
                    wireIntersects.add(wire);
                }
                if(wire.end.equals(pos)){
                    wire.endWI=this;
                    wireIntersects.add(wire);
                }
                if(wire.pointInside(pos, 1)){
                    wireIntersects.add(wire);
                }
            }
            pageWireIntersections.stream().filter((t) -> {
                return t.pos.equals(pos);
            }).findFirst().ifPresent((t) -> {
                throw new IllegalStateException();
            });
            if(!wireIntersects.isEmpty())
                pageWireIntersections.add(this);
        }
        
        public static WireIntersection getWI(Vector2i p){
            WireIntersection wi = getWIAt(p);
            if(wi==null){
                wi=new WireIntersection(p);
            }
            return wi;
        }
        
        public void draw(Graphics g){
            Vector2f t0 = gridToScreen(pos);
            drawOval(g, t0.x, t0.y, zoomGridGap, zoomGridGap);
        }
        
        public static WireIntersection getWIAt(Vector2i p){
            Optional<WireIntersection> m = pageWireIntersections.stream().filter((t) -> {
                return t.pos.equals(p);
            }).findFirst();
            return m.orElse(null);
        }
        
        public void checkIsEmpty(){
            if(wireIntersects.isEmpty())
                delete();
        }
        
        public void delete(){
            pageWireIntersections.remove(this);
        }
       
    }
    
    public static class Wire{
        Vector2i start=new Vector2i();
        Vector2i end=new Vector2i();
        WireIntersection endWI = null;
        WireIntersection startWI = null;
        Color color = Color.BLACK;
        boolean deleted = false;
        
        public Wire() {
        }

        public static Wire createDrawWire(){
            return new Wire();
        }
        
        public static Wire create(Vector2i s, Vector2i e){
            Wire w = new Wire();
            w.start =s;
            w.end =e;
            int temp=0;
            if(w.start.x>w.end.x){
                temp=w.start.x;
                w.start.x=w.end.x;
                w.end.x=temp;
            }
            if(w.start.y>w.end.y){
                temp=w.start.y;
                w.start.y=w.end.y;
                w.end.y=temp;
            }
            w.color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
            
            w.checkIntersections();
            
            
            return w;
        }
        
        public void delete(){
            pageWireIntersections.stream().forEach((t) -> {
                t.wireIntersects.remove(this);
                t.checkIsEmpty();
            });
            pageWires.remove(this);
            deleted = true;
        }
        
        public void checkIntersections(){
            ArrayList<Wire> wiresTemp = new ArrayList<>(pageWires);
            
            for (Wire w : wiresTemp) {
                if(w.equals(this)) continue;
                if(w.deleted) continue;
                if(w.isHorizontal()==isHorizontal()){
                    if(w.start.equals(end)){
                        if(w.startWI.wireIntersects.size()<=1){
                            w.start=start;
                            delete();
                            w.checkIntersections();
                            return;
                        }
                    }
                    else if(w.end.equals(start)){
                        if(w.endWI.wireIntersects.size()<=1){
                            w.end=end;
                            delete();
                            w.checkIntersections();
                            return;
                        }
                    }
                }else{
                    if(w.pointInside(start, 1)){
                        w.split(start);
                    }
                    else if(w.pointInside(end, 1)){
                        w.split(end);
                    }
                }
            }
            
            if(!deleted){
                pageWires.add(this);
            
                if(startWI!=null){
                    startWI.wireIntersects.remove(this);
                    startWI.checkIsEmpty();
                }
                if(endWI!=null){
                    endWI.wireIntersects.remove(this);
                    endWI.checkIsEmpty();
                }
                startWI = WireIntersection.getWI(start);
                startWI.wireIntersects.add(this);
                endWI = WireIntersection.getWI(end);
                endWI.wireIntersects.add(this);
            }
            
        }
        
        public void draw(Graphics2D g){
            Vector2f t0 = gridToScreenCenter(start);
            Vector2f t1 = gridToScreenCenter(end);
            g.setColor(color);
            g.setStroke(new BasicStroke(2));
            drawLine(g, t0.x, t0.y, t1.x, t1.y);
        }
        
        public Vector2i getVec(){
            return new Vector2i(end).sub(start);
        }
        
        public float getLen(){
            return (float) getVec().length();
        }
        
        public boolean intersectPoint(Vector2i p){
            Vector2f t = new Vector2f();
            boolean r=Line2D.ptSegDist(start.x, start.y, end.x, end.y, p.x, p.y)==0;
            return r;
        }
        
        public boolean isHorizontal(){
            return getVec().y==0;
        }
        
        public void connect(Wire w1){
        }
        
        public void split(Vector2i p){
            if(!intersectPoint(p)){
                throw new IllegalArgumentException("Tried to split at a point not on wire");
            }
            if(p.equals(start) || p.equals(end)) return;
            
            Vector2i temp = end;
            end=p;
            Wire w1 = Wire.create(p,temp);
        }
        
        public boolean pointInside(Vector2i p, int offset){
            if(getLen()<2) return false;
            
            Vector2f t = new Vector2f();
            boolean r = false;
            if(isHorizontal()){
                r=Line2D.ptSegDist(start.x+offset, start.y, end.x-offset, end.y, p.x, p.y)==0;
            } else{
                r=Line2D.ptSegDist(start.x, start.y+offset, end.x, end.y-offset, p.x, p.y)==0;
            }
            return r;
        }
        
        public boolean containsWire(Wire w1){
            if(getLen()<2) return false;
            if(w1.getLen()<1) return false;
            if(isHorizontal()!=w1.isHorizontal()) return false;
            
            if(pointInside(w1.start, 1) || pointInside(w1.end, 1)){
                return true;
            }
            
            return false;
        }
        
        public boolean intersectWire(Wire w1){
            return intersectWire(w1, null);
        }
        
        public boolean intersectWire(Wire w1, Vector2i i){
            Vector2f t = new Vector2f();
            boolean r = Intersectionf.intersectLineLine(start.x, start.y, end.x, end.y, w1.start.x, w1.start.y, w1.end.x, w1.end.y, t);
            if(i!=null)
                i = new Vector2i((int)t.x, (int)t.y);
            return r;
        }
    }
    
    public static enum DrawMode{
        NONE,
        LINE,
        ELEMENT
    }
    
    public static void init(){
        Dimension scDim = Toolkit.getDefaultToolkit().getScreenSize();
        Vector2f scSz = new Vector2f(scDim.width, scDim.height);
        
        float percent = 0.6f;
        Vector2f newWinSize = new Vector2f(scSz.x * percent, scSz.y * percent);
        Vector2f newWinPos = new Vector2f((scSz.x-scSz.x * percent) / 2,
                (scSz.y-scSz.y * percent) / 2);
        
        mf.setSize((int)newWinSize.x, (int)newWinSize.y);
        mf.setLocation((int)newWinPos.x, (int)newWinPos.y);
        
        /*
        int mul = 16;
        bi = new BufferedImage(pageSize.x*mul,pageSize.y*mul,BufferedImage.TYPE_INT_ARGB);
        Graphics2D gi = (Graphics2D)bi.getGraphics();
        gi.setColor(new Color(0, 0, 0, 0));
        gi.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        gi.setColor(new Color(0.8f, 0.8f, 0.8f));
        gi.setStroke(new BasicStroke(1));
        for (int i = 0; i < bi.getHeight(); i+=gridGap*mul) {
            gi.drawLine(0, i, bi.getWidth(), i);
        }
        for (int i = 0; i < bi.getWidth(); i+=gridGap*mul) {
            gi.drawLine(i, 0, i, bi.getHeight());
        }
        */
    }
    
    public static void mouseEvent(){
        Logic.zoom-=Mouse.wheel*0.1f;
        zoomGridGap = zoom * gridGap;
        cursorPosPage = new Vector2i(Mouse.pos).sub(pagePos);
        cursorGridMoved=false;
        if(cursorPosGrid != new Vector2i(cursorPosPage).div(zoomGridGap)){
            cursorPosGrid = new Vector2i(cursorPosPage).div(zoomGridGap);
            cursorGridMoved=true;
            cursorPosPageGridSnap = new Vector2f((cursorPosGrid.x*zoomGridGap), (cursorPosGrid.y*zoomGridGap)).add(pagePos.x, pagePos.y);
        }
        
        if(Mouse.isDown(1)){
            if(!isDrawing){
                isDrawing = true;
                dw.start = cursorPosGrid;
                dwGood = false;
            }
            
            if(isDrawing && cursorGridMoved){
                dw.end = cursorPosGrid;
                Vector2i dpvec = dw.getVec();
                if(Math.abs(dpvec.x) > Math.abs(dpvec.y)){
                    dw.end.y=dw.start.y;
                }else{
                    dw.end.x=dw.start.x;
                }
                
                dwGood=true;
                for (Wire w : pageWires) {
                    if(w.containsWire(dw)){
                        dwGood=false;
                        break;
                    }
                }
                if(dw.getLen()==0){
                    dwGood=false;
                }
            }
        }else{
            if(isDrawing){
                isDrawing = false;
                
                if(dwGood){
                    Wire nw = Wire.create(dw.start, dw.end);
                }
            }
        }
        
        if(Mouse.isDown(2)){
            pagePos.x-=Mouse.rel.x;
            pagePos.y-=Mouse.rel.y;
        }
        
        ep.repaint();
        Mouse.reset();
    }
    
    public static void winResized(){
        Vector2i eps = new Vector2i(ep.getWidth(), ep.getHeight());
        Vector2i epsof = new Vector2i(eps.x-pageSize.x, eps.y-pageSize.y);
        
        if(epsof.x<epsof.y){
            zoom = 1.0f * eps.x / pageSize.x;
        }else{
            zoom = 1.0f * eps.y / pageSize.y;
        }
        zoomGridGap = zoom * gridGap;
        ep.repaint();
    }
    
    public static void drawLine(Graphics g, float x, float y, float x1, float y1){
        g.drawLine(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }
    
    public static void drawOval(Graphics g, float x, float y, float x1, float y1){
        g.drawOval(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }
    
    public static Vector2f gridToScreenCenter(Vector2i v){
        return gridToScreenCenter(v.x, v.y);
    }
    
    public static Vector2f gridToScreenCenter(int x, int y){
        return new Vector2f(pagePos.x+(x*zoomGridGap)+zoomGridGap/2, pagePos.y+(y*zoomGridGap)+zoomGridGap/2);
    }
    
    public static Vector2f gridToScreen(Vector2i v){
        return gridToScreen(v.x, v.y);
    }
    
    public static Vector2f gridToScreen(int x, int y){
        return new Vector2f(pagePos.x+(x*zoomGridGap), pagePos.y+(y*zoomGridGap));
    }
    
    public static void drawEditPanel(Graphics gr){
        EditPanel ep = EditPanel.ins;
        Graphics2D g = (Graphics2D)gr;
        
        g.setColor(new Color(0.8f, 0.8f, 0.8f));
        float newGap = zoomGridGap;
        Vector2i offset = new Vector2i(pagePos);
        for (float i = 0; i < pageSize.y * zoom; i+=zoomGridGap) {
            drawLine(g, offset.x, offset.y+i, offset.x+(pageSize.x * zoom), offset.y+i);
        }
        for (float i = 0; i < pageSize.x * zoom; i+=zoomGridGap) {
            drawLine(g, offset.x+i,  offset.y, offset.x+i, offset.y+(pageSize.y * zoom));
        }
        
        //g.drawImage(bi, pagePos.x, pagePos.y, Math.round(pageSize.x*zoom), Math.round(pageSize.y*zoom), null);
        
        g.setColor(Color.red);
        g.drawOval(Math.round(cursorPosPageGridSnap.x), Math.round(cursorPosPageGridSnap.y), Math.round(zoomGridGap), Math.round(zoomGridGap));
        
        g.setColor(Color.BLACK);
        
        pageWires.stream().forEach((t) -> {
            t.draw(g);
        });
        
        g.setColor(Color.BLACK);
        pageWireIntersections.stream().forEach((t) -> {
            t.draw(g);
        });
        
        g.setStroke(new BasicStroke(2));
        if(isDrawing){
            g.setColor(Color.RED);
            if(dwGood){
                g.setColor(Color.GREEN);
            }
            dw.draw(g);
        }
    }
    
}
