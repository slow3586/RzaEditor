package rzaeditor;

import rzaeditor.drawmodes.DrawMode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.function.Consumer;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglef;
import org.joml.primitives.Rectanglei;
import rzaeditor.pageobjects.PageObjectBase;

public class Logic {

    public static float zoomGridGap = 6;
    public static float zoom = 1.0f;
    public static Vector2i windowSize = new Vector2i(640,480);
    public static MainFrame mf = MainFrame.imp;
    public static EditPanel ep = EditPanel.imp;
    public static boolean isDrawing = false;
    public static Vector2i dragVector = new Vector2i();
    public static CreationMode creationMode = CreationMode.PAGE;
    public static Vector2i dragStart = new Vector2i();
    public static Vector2i dragEnd = new Vector2i();
    public static Vector2i dragStartFixed = new Vector2i();
    public static Vector2i dragEndFixed = new Vector2i();
    public static Vector2i dragVec = new Vector2i();
    public static Vector2i dragVecFixed = new Vector2i();
    public static Rectanglei dragRect = new Rectanglei();
    public static boolean isDragging = false;
            
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
        mf.revalidate();
    }
    
    public static void keyboardEvent(){
        DrawMode.getCurrent().keyboardEvent();
        ep.repaint();
        Keyboard.reset();
    }
    
    public static Vector2i swapIfTrue(int x, int y, boolean b){
        if(b)
            return new Vector2i(y, x);
        else
            return new Vector2i(x, y);
    }
    
    public static Vector2i swapIfTrue(Vector2i v, boolean b){
        if(b)
            return new Vector2i(v.y, v.x);
        else
            return new Vector2i(v.x, v.y);
    }
    
    public static void mouseEvent(){
        
        Logic.zoom-=Mouse.wheel*0.1f;
        zoomGridGap = zoom * Page.gridGap;
        MainFrame.zoomLabel.setText("Zoom: "+Math.round(Logic.zoom*100)
                +"% X:"+Cursor.posGrid.x*Page.current.cmPerCell
                +"???? Y:"+Cursor.posGrid.y*Page.current.cmPerCell+"???? "+
                DrawMode.getCurrent().infoText);
        
        rzaeditor.Cursor.updateCursor();
        
        dragEnd = new Vector2i(Cursor.posGrid);
        
        dragStartFixed = new Vector2i(dragStart);
        dragEndFixed = new Vector2i(dragEnd);
        Logic.fixVectorPositions(dragStartFixed, dragEndFixed);
        
        dragRect.setMin(dragStartFixed);
        dragRect.setMax(dragEndFixed);
        dragVec = new Vector2i(dragEnd).sub(dragStart);
        dragVecFixed = new Vector2i(dragEndFixed).sub(dragStartFixed);
        
        if(Mouse.isPressed(1)){
            dragStart = new Vector2i(Cursor.posGrid);
            dragEnd = new Vector2i(Cursor.posGrid);
            
            dragStartFixed = new Vector2i(dragStart);
            dragEndFixed = new Vector2i(dragEnd);
            Logic.fixVectorPositions(dragStartFixed, dragEndFixed);
            
            dragRect.setMin(dragStart);
            dragRect.setMax(dragEnd);
            isDragging = true;
            
            DrawMode.getCurrent().mousePressed();
        }
            
        if(Mouse.isReleased(1)){
            DrawMode.getCurrent().mouseReleased();
            
            isDragging = false;
            dragStart.zero();
            dragEnd.zero();
            dragRect.setMin(0,0);
            dragRect.setMax(0,0);
        }
        
        if(Cursor.gridMoved){
            if(Mouse.isDown(1)){
                DrawMode.getCurrent().mouseDrag();
            }else{
                DrawMode.getCurrent().mouseMove();
            }
        }
        if(Mouse.isDown(2)){
            Page.current.pos.x-=Mouse.rel.x;
            Page.current.pos.y-=Mouse.rel.y;
        }
        
        ep.repaint();
        Mouse.reset();
    }
    
    public static void winResized(){
        Vector2i eps = new Vector2i(ep.getWidth(), ep.getHeight());
        Vector2i epsof = new Vector2i(eps.x-Page.current.size.x, eps.y-Page.current.size.y);
        
        if(epsof.x<epsof.y){
            zoom = 1.0f * eps.x / Page.current.size.x;
        }else{
            zoom = 1.0f * eps.y / Page.current.size.y;
        }
        zoomGridGap = zoom * Page.current.gridGap;
        ep.repaint();
    }
    
    public static boolean isInsideGrid(Vector2i v){
        return v.x>=0 && v.y>=0 && v.x<=Page.current.gridSize.x && v.y<=Page.current.gridSize.y;
    }
    
    public static Vector2i gridToScreenCenter(Vector2i v){
        return gridToScreenCenter(v.x, v.y);
    }
    
    public static Vector2i gridToScreenCenter(int x, int y){
        return new Vector2i(Math.round((x*zoomGridGap)-zoomGridGap/2), Math.round((y*zoomGridGap)-zoomGridGap/2));
    }
    
    public static int posToScreen(float x){
        return Math.round(x*zoom);
    }
    
    public static int gridToScreen(float x){
        return Math.round(x*zoomGridGap);
    }
    
    public static Vector2i gridToScreen(Vector2i v){
        return gridToScreen(v.x, v.y);
    }
    
    public static Vector2i gridToScreen(int x, int y){
        return new Vector2i(Math.round((x*zoomGridGap)), Math.round((y*zoomGridGap)));
    }
    
    public static Vector2i sizeToScreen(float x, float y){
        return new Vector2i(Math.round(x*zoomGridGap), Math.round(y*zoomGridGap));
    }
    
    public static Vector2i posToScreen(float x, float y){
        return new Vector2i(Math.round(x*zoom), Math.round(y*zoom));
    }
    
    
    
    public static void fixVectorPositions(Vector2i start, Vector2i end){
        Vector2i s = new Vector2i(Math.min(start.x, end.x),Math.min(start.y, end.y));
        Vector2i e = new Vector2i(Math.max(start.x, end.x),Math.max(start.y, end.y));
        
        start.x = s.x;
        start.y = s.y;
        end.x = e.x;
        end.y = e.y;
    }
    
    
}
