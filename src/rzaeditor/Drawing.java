package rzaeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class Drawing {
    
    public static Graphics2D g;

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

    public static void drawEditPanel(Graphics gr) {
        g = (Graphics2D) gr;
        EditPanel ep = EditPanel.ins;
        g.setColor(new Color(0.8F, 0.8F, 0.8F));
        float newGap = Logic.zoomGridGap;
        Vector2i offset = new Vector2i(Page.pos);
        for (float i = 0; i < Page.size.y * Logic.zoom; i += Logic.zoomGridGap) {
            drawLine(offset.x, offset.y + i, offset.x + (Page.size.x * Logic.zoom), offset.y + i);
        }
        for (float i = 0; i < Page.size.x * Logic.zoom; i += Logic.zoomGridGap) {
            drawLine(offset.x + i, offset.y, offset.x + i, offset.y + (Page.size.y * Logic.zoom));
        }
        g.setColor(Color.red);
        g.drawOval(Math.round(Cursor.posPageGridSnap.x), Math.round(Cursor.posPageGridSnap.y), Math.round(Logic.zoomGridGap), Math.round(Logic.zoomGridGap));
        g.setColor(Color.BLACK);
        Page.wires.stream().forEach((Wire t) -> {
            t.draw(g);
        });
        
        Logic.drawMode.draw();
        
        g.setColor(Color.BLACK);
        Page.wireIntersections.stream().forEach((WireIntersection t) -> {
            t.draw(g);
        });
        g.setStroke(new BasicStroke(2));
    }

}
