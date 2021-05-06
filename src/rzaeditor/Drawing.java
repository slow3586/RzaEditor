package rzaeditor;

import rzaeditor.pageobjects.PageObject;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.WireIntersection;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Drawing {
    
    private static Graphics2D g;
    private static Vector2i translate = new Vector2i();
    
    public static void drawLine(Vector2i s, Vector2i e) {
        drawLine(s.x, s.y, e.x, e.y);
    }
    
    public static void drawLine(float x, float y, float x1, float y1) {
        g.drawLine(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }
    
    public static void drawLineGrid(int x, int y, int x1, int y1) {
        g.drawLine(Math.round(x*Logic.zoomGridGap), Math.round(y*Logic.zoomGridGap), 
                Math.round(x1*Logic.zoomGridGap), Math.round(y1*Logic.zoomGridGap));
    }

    public static void drawOval(float x, float y, float x1, float y1) {
        g.drawOval(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }

    public static void fillOval(float x, float y, float x1, float y1) {
        g.fillOval(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }

    public static void drawRect(Vector2i s, Vector2i e) {
        drawRect(s.x, s.y, e.x, e.y);
    }
    
    public static void drawRectGrid(int x, int y, int w, int h) {
        g.drawRect(Math.round(x*Logic.zoomGridGap), Math.round(y*Logic.zoomGridGap),
                Math.round(w*Logic.zoomGridGap), Math.round(h*Logic.zoomGridGap));
    }
    
    public static void drawRect(float x, float y, float w, float h) {
        g.drawRect(Math.round(x), Math.round(y), Math.round(w), Math.round(h));
    }
    
    public static void drawArc(Vector2i p, Vector2i s, float startAngle, float endAngle) {
        drawArc(p.x, p.y, s.x, s.y, startAngle, endAngle);
    }
    
    public static void drawArc(float x, float y, float w, float h, float startAngle, float endAngle) {
        g.drawArc(Math.round(x), Math.round(y), Math.round(w), Math.round(h), Math.round(startAngle), Math.round(endAngle));
    }
    
    public static void drawString(String str, float x, float y) {
        g.drawString(str, x, y);
    }
    
    public static void setStroke(float s){
        g.setStroke(new BasicStroke(s));
    }
    
    public static void setColor(Color c){
        g.setColor(c);
    }
    
    public static void resetTransform(){
        g.setTransform(new AffineTransform());
    }
    
    public static void setTranslate(int x, int y){
        g.setTransform(AffineTransform.getTranslateInstance(x, y));
    }
    
    public static void setTranslateGrid(Vector2i p){
        setTranslateGrid(p.x, p.y);
    }
    
    public static void setTranslateGrid(int x, int y){
        g.setTransform(AffineTransform.getTranslateInstance(Page.current.pos.x+x*Logic.zoomGridGap, Page.current.pos.y+y*Logic.zoomGridGap));
    }
    
    public static void setRot(float angle){
        g.rotate(Math.toRadians(angle));
    }
    
    public static void drawWire(){
        Vector2i off0 = new Vector2i(Page.current.pos);
        
        resetTransform();
        
        int every = 4;
        if(Page.current.useFineGrid) 
            every=2;
        Color c0 = new Color(0.8f,0.8f,0.8f);
        Color c1 = new Color(0.6f,0.6f,0.6f);
        Color cur = c1;
        float off1 = 0;
        for (int i = 1; i<=Math.max(Page.current.gridSize.x, Page.current.gridSize.y); i++){
            off1 = i * Page.gridGap * Logic.zoom;
            cur = c0;
            if(i%every==0){
                cur = c1;
                if(Page.current.useFineGrid)
                    drawString(String.valueOf(i*Page.current.cmPerCell), off0.x+off1-5, off0.y-10);
            }
            setColor(cur);
            if(i<=Page.current.gridSize.y)
                drawLine(off0.x, off0.y + off1, off0.x + (Page.current.size.x * Logic.zoom), off0.y + off1);
            if(i<=Page.current.gridSize.x)
                drawLine(off0.x + off1, off0.y, off0.x + off1, off0.y + (Page.current.size.y * Logic.zoom));
        }
        
        setColor(new Color(1,1,1));
        setStroke(1);
        drawRect(off0.x, off0.y, Page.current.size.x * Logic.zoom, Page.current.size.y * Logic.zoom);
    }

    public static void drawEditPanel(Graphics gr) {
        g = (Graphics2D) gr;
        EditPanel ep = EditPanel.imp;
        
        drawWire();
        
        Cursor.draw();
        
        Drawing.setColor(Color.BLACK);
        Page.current.primitives.stream().forEach((PageObject t) -> {
            t.draw();
        });
        
        Page.current.wires.stream().forEach((Wire t) -> {
            t.draw();
        });
        
        Logic.drawMode.draw();
        
        Drawing.setColor(Color.BLACK);
        Page.current.wireIntersections.stream().forEach((WireIntersection t) -> {
            t.draw();
        });
        
        Page.current.objects.stream().forEach((t) -> {
            t.draw();
        });
        
        Drawing.resetTransform();
    }

}
