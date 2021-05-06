package rzaeditor.pageobjects;

import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.zoomGridGap;
import rzaeditor.Page;

public class PageRect extends Primitive {

    Vector2i pos = new Vector2i();
    Vector2i size = new Vector2i();
    Color color = Color.BLACK;
    
    PageRect() {
        name = "Прямоугольник "+Page.current.wires.size();
        ID = "Прямоугольник "+Page.current.wires.size();
        type = "Прямоугольник";
    }
    
    public static PageRect create(Vector2i pos, Vector2i size){
        PageRect pl = new PageRect();
        pl.pos = new Vector2i(pos);
        pl.size = new Vector2i(size);
        
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
        //Drawing.drawRect(Logic.gridToScreen(pos));
        Drawing.drawRect(Logic.gridToScreen(pos), new Vector2i(Math.round(size.x*zoomGridGap), Math.round(size.y*zoomGridGap))); 
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
