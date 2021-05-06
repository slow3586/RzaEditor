package rzaeditor.pageobjects;

import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public class PageLine extends Primitive {

    Vector2i start = new Vector2i();
    Vector2i end = new Vector2i();
    Color color = Color.BLACK;
    
    PageLine() {
        name = "Линия "+Page.current.wires.size();
        ID = "Линия "+Page.current.wires.size();
        type = "Линия";
    }
    
    public static PageLine create(Vector2i s, Vector2i e){
        PageLine pl = new PageLine();
        pl.start = new Vector2i(s);
        pl.end = new Vector2i(e);
        Logic.fixVectorPositions(pl.start, pl.end);
        
        Page.current.primitives.add(pl);
        return pl;
    }
    
    public static boolean canBePlacedAt(Vector2i s, Vector2i e){
        Vector2i vec = new Vector2i(e).sub(s);
        
        if(vec.length()<=0) return false;
        
        for (PageObject primitive : Page.current.primitives) {
            if(primitive instanceof PageLine){
            
            }
        }
        
        for (Wire w1 : Page.current.wires) {
            if ((vec.y == 0)==w1.isHorizontal() && (w1.pointInside(s, 1) || w1.pointInside(e, 1))) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void draw() {
        Drawing.setColor(color);
        if(selected){
            Drawing.setColor(Color.RED);
            Drawing.setStroke(3);
        }
        Drawing.setStroke(2);
        Drawing.drawLine(Logic.gridToScreen(start), Logic.gridToScreen(end));
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
