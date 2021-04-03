package rzaeditor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglef;
import org.joml.primitives.Rectanglei;

public class Logic {

    public static int gridGap = 6;
    public static Vector2i pageSizeNoBorder = new Vector2i(420,297);
    public static Vector2i pageSize = new Vector2i(395, 287);
    public static Vector2i pageGridSize = new Vector2i(pageSize.x/gridGap, pageSize.y/gridGap);
    public static Rectanglei pageRect = new Rectanglei(-1,-1,pageGridSize.x,pageGridSize.y);
    public static Vector2i pagePos = new Vector2i(0,0);
    public static Vector2i pageTitleSize = new Vector2i(185,55);
    public static float zoomGridGap = 6;
    public static float zoom = 1.0f;
    public static Vector2i windowSize = new Vector2i(640,480);
    public static MainFrame mf = MainFrame.ins;
    public static EditPanel ep = EditPanel.ins;
    public static boolean isDrawing = false;
    public static Vector2i cursorPosPage = new Vector2i();
    public static Vector2i cursorPosGrid = new Vector2i();
    public static Vector2f cursorPosPageGridSnap = new Vector2f();
    public static BufferedImage bi;
    public static DrawMode dm = DrawMode.NONE;
    public static boolean dwGood = false;
    public static HashSet<Wire> pageWires = new HashSet<>();
    public static HashSet<WireIntersection> pageWireIntersections = new HashSet<>();
    public static Wire dw = Wire.createDrawWire();
    public static boolean cursorGridMoved = false;
    
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
    
    public static void updateCursor(){
        cursorPosPage = new Vector2i(Mouse.pos).sub(pagePos);
        cursorGridMoved=false;
        if(cursorPosGrid != new Vector2i(cursorPosPage).div(zoomGridGap)){
            cursorPosGrid = new Vector2i(cursorPosPage).div(zoomGridGap);
            cursorGridMoved=true;
            cursorPosPageGridSnap = new Vector2f((cursorPosGrid.x*zoomGridGap), (cursorPosGrid.y*zoomGridGap)).add(pagePos.x, pagePos.y);
        }
    }
    
    public static void drawLineMode(){
        Vector2i dpvec = dw.getVec();
        if(Math.abs(dpvec.x) > Math.abs(dpvec.y))
            dw.end.y=dw.start.y;
        else
            dw.end.x=dw.start.x;

        dwGood=dw.getLen()!=0;
        for (Wire w : pageWires) {
            if(w.containsWire(dw)){
                dwGood=false;
                break;
            }
        }
    }
    
    public static void selectMode(){
        dw.end.add(1, 1);
        Rectanglei r = new Rectanglei(dw.start, dw.end);
        for (Wire w : pageWires) {
            w.selected=r.containsPoint(w.getCenter());
        }
    }
    
    public static void mouseEvent(){
        Logic.zoom-=Mouse.wheel*0.1f;
        zoomGridGap = zoom * gridGap;
        
        updateCursor();
        
        if(Mouse.isDown(1) && pageRect.containsPoint(cursorPosGrid)){
                if(!isDrawing){
                    isDrawing = true;
                    dw.start = cursorPosGrid;
                    dwGood = false;
                }

                if(isDrawing && cursorGridMoved){
                    dw.end = cursorPosGrid;
                    if(dm==DrawMode.LINE){
                        drawLineMode();
                    }else{
                        selectMode();
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
    
    public static void drawRect(Graphics g, float x, float y, float w, float h){
        g.drawRect(Math.round(x), Math.round(y), Math.round(w), Math.round(h));
    }
    
    public static void drawOval(Graphics g, float x, float y, float x1, float y1){
        g.drawOval(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }
    
    public static void fillOval(Graphics g, float x, float y, float x1, float y1){
        g.fillOval(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
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
            if(dm==DrawMode.LINE){
                g.setColor(Color.RED);
                if(dwGood){
                    g.setColor(Color.GREEN);
                }
                dw.draw(g);
            }else{
                g.setColor(Color.CYAN);
                Vector2f t0 = Logic.gridToScreen(dw.start);
                Vector2f t1 = Logic.gridToScreen(dw.getVec());
                drawRect(g, t0.x, t0.y, t1.x, t1.y);
            }
        }
    }
    
}
