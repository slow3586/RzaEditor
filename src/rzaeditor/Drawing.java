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

    public static void drawEditPanel(Graphics gr) {
        g = (Graphics2D) gr;
        EditPanel ep = EditPanel.imp;
        g.setColor(new Color(0.8F, 0.8F, 0.8F));
        float newGap = Logic.zoomGridGap;
        Vector2i offset = new Vector2i(Page.current.pos);
        for (float i = 0; i < Page.current.size.y * Logic.zoom; i += Logic.zoomGridGap) {
            drawLine(offset.x, offset.y + i, offset.x + (Page.current.size.x * Logic.zoom), offset.y + i);
        }
        for (float i = 0; i < Page.current.size.x * Logic.zoom; i += Logic.zoomGridGap) {
            drawLine(offset.x + i, offset.y, offset.x + i, offset.y + (Page.current.size.y * Logic.zoom));
        }
        g.setColor(Color.red);
        drawOval(Cursor.posPageGridSnap.x, Cursor.posPageGridSnap.y, Logic.zoomGridGap, Logic.zoomGridGap);
        g.setColor(Color.BLACK);
        Page.current.primitives.stream().forEach((PageObject t) -> {
            t.draw();
        });
        
        Page.current.wires.stream().forEach((Wire t) -> {
            t.draw();
        });
        
        Logic.drawMode.draw();
        
        g.setColor(Color.BLACK);
        Page.current.wireIntersections.stream().forEach((WireIntersection t) -> {
            t.draw();
        });
        g.setStroke(new BasicStroke(2));
    }

}
