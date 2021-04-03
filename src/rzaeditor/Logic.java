package rzaeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import org.joml.Vector2f;
import org.joml.Vector2i;

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
    public static Vector2i dp0 = new Vector2i();
    public static Vector2i dp1 = new Vector2i();
    public static BufferedImage bi;
    public static DrawMode dm = DrawMode.NONE;
    
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
        cursorPosGrid = new Vector2i(cursorPosPage).div(zoomGridGap);
        cursorPosPageGridSnap = new Vector2f((cursorPosGrid.x*zoomGridGap), (cursorPosGrid.y*zoomGridGap)).add(pagePos.x, pagePos.y);
        
        if(Mouse.isDown(1)){
            if(!isDrawing){
                isDrawing = true;
                dp0 = cursorPosGrid;
            }
            
            if(isDrawing){
                dp1 = cursorPosGrid;
                Vector2i dpvec = new Vector2i(dp1).sub(dp0);
                System.out.println(dp1);
                if(Math.abs(dpvec.x) > Math.abs(dpvec.y)){
                    dp1.y=dp0.y;
                }else{
                    dp1.x=dp0.x;
                }
            }
        }else{
            isDrawing = false;
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
        System.out.println(zoomGridGap);
        ep.repaint();
    }
    
    public static void drawLine(Graphics g, float x, float y, float x1, float y1){
        g.drawLine(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }
    
    public static void drawEditPanel(Graphics g){
        EditPanel ep = EditPanel.ins;
        
        
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
        
        drawLine(g,zoomGridGap/2+offset.x+dp0.x*zoomGridGap, zoomGridGap/2+dp0.y*zoomGridGap, zoomGridGap/2+dp1.x*zoomGridGap, zoomGridGap/2+dp1.y*zoomGridGap);
    }
    
}
