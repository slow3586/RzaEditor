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
    public static float zoomGridGap = 6;
    public static float zoom = 1.0f;
    public static Vector2i windowSize = new Vector2i(640,480);
    public static MainFrame mf = MainFrame.ins;
    public static EditPanel ep = EditPanel.ins;
    public static boolean isDrawing = false;
    public static DrawMode drawMode = DrawModeWire.ins;
    public static Vector2i dragVector = new Vector2i();
    public static CreationMode creationMode = CreationMode.PAGE;
            
    public static enum CreationMode{
        PAGE,
        OBJECT
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
        
        rzaeditor.Cursor.updateCursor();
        
        if(Mouse.isPressed(1))
            drawMode.mousePressed();
            
        if(Mouse.isReleased(1))
            drawMode.mouseReleased();
        
        if(Cursor.gridMoved){
            if(Mouse.isDown(1)){
                drawMode.mouseDrag();
            }else{
                drawMode.mouseMove();
            }
        }
        if(Mouse.isDown(2)){
            Page.pos.x-=Mouse.rel.x;
            Page.pos.y-=Mouse.rel.y;
        }
        
        ep.repaint();
        Mouse.reset();
    }
    
    public static void winResized(){
        Vector2i eps = new Vector2i(ep.getWidth(), ep.getHeight());
        Vector2i epsof = new Vector2i(eps.x-Page.size.x, eps.y-Page.size.y);
        
        if(epsof.x<epsof.y){
            zoom = 1.0f * eps.x / Page.size.x;
        }else{
            zoom = 1.0f * eps.y / Page.size.y;
        }
        zoomGridGap = zoom * gridGap;
        ep.repaint();
    }
    
    
    public static Vector2f gridToScreenCenter(Vector2i v){
        return gridToScreenCenter(v.x, v.y);
    }
    
    public static Vector2f gridToScreenCenter(int x, int y){
        return new Vector2f(Page.pos.x+(x*zoomGridGap)+zoomGridGap/2, Page.pos.y+(y*zoomGridGap)+zoomGridGap/2);
    }
    
    public static Vector2f gridToScreen(Vector2i v){
        return gridToScreen(v.x, v.y);
    }
    
    public static Vector2f gridToScreen(int x, int y){
        return new Vector2f(Page.pos.x+(x*zoomGridGap), Page.pos.y+(y*zoomGridGap));
    }
    
    
}
