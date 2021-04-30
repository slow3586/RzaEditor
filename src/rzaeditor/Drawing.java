package rzaeditor;

import rzaeditor.pageobjects.PageObject;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.WireIntersection;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Drawing {
    
    private static Graphics2D g;
    
    public static void drawLine(Vector2i s, Vector2i e) {
        drawLine(s.x, s.y, e.x, e.y);
    }
    
    public static void drawLine(float x, float y, float x1, float y1) {
        g.drawLine(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }

    public static void drawOval(float x, float y, float x1, float y1) {
        g.drawOval(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }

    public static void fillOval(float x, float y, float x1, float y1) {
        g.fillOval(Math.round(x), Math.round(y), Math.round(x1), Math.round(y1));
    }

    public static void drawRect(float x, float y, float w, float h) {
        g.drawRect(Math.round(x), Math.round(y), Math.round(w), Math.round(h));
    }
    
    public static void setStroke(float s){
        g.setStroke(new BasicStroke(s));
    }
    
    public static void setColor(Color c){
        g.setColor(c);
    }
    
    public static void drawWire(){
        Vector2i offset = new Vector2i(Page.current.pos);
        
        if(Page.current.useFineGrid){
            setColor(new Color(0.9F, 0.9F, 0.9F));
            for (float i = 0; i < Page.current.size.y * Logic.zoom; i += Page.gridGap/2 * Logic.zoom) {
                drawLine(offset.x, offset.y + i, offset.x + (Page.current.size.x * Logic.zoom), offset.y + i);
            }
            for (float i = 0; i < Page.current.size.x * Logic.zoom; i += Page.gridGap/2 * Logic.zoom) {
                drawLine(offset.x + i, offset.y, offset.x + i, offset.y + (Page.current.size.y * Logic.zoom));
            }
        }
        
        
        int count = 3;
        for (float i = 0; i < Page.current.size.y * Logic.zoom; i += Page.gridGap * Logic.zoom) {
            count++;
            setColor(new Color(0.8F, 0.8F, 0.8F));
            if(count==4){
                setColor(new Color(0.6f, 0.6f, 0.6f));
                count = 0;
            }
            drawLine(offset.x, offset.y + i, offset.x + (Page.current.size.x * Logic.zoom), offset.y + i);
        }
        count = 3;
        for (float i = 0; i < Page.current.size.x * Logic.zoom; i += Page.gridGap * Logic.zoom) {
            count++;
            setColor(new Color(0.8F, 0.8F, 0.8F));
            if(count==4){
                setColor(new Color(0.6f, 0.6f, 0.6f));
                count = 0;
            }
            drawLine(offset.x + i, offset.y, offset.x + i, offset.y + (Page.current.size.y * Logic.zoom));
        }
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
    }

}
