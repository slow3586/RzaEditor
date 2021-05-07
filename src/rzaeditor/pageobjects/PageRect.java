package rzaeditor.pageobjects;

import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.zoomGridGap;
import rzaeditor.Page;

public class PageRect extends Primitive {
    public PageRect(Vector2i pos) {
        super(pos);
    }
    
    @Override
    public String getType() {
        return "Прямоугольник";
    }
    
    public static PageRect create(Vector2i pos, Vector2i size){
        PageRect pl = new PageRect(pos);
        pl.setSize(new Vector2i(size));
        
        Page.current.primitives.add(pl);
        return pl;
    }
    
    public static boolean canBePlacedAt(Vector2i s, Vector2i e){
        Vector2i vec = new Vector2i(e).sub(s);
        
        if(vec.length()<=0) return false;
        
        for (Wire w1 : Page.current.wires) {
            if ((vec.y == 0)==w1.isHorizontal() && (w1.pointInside(s, 1) || w1.pointInside(e, 1))) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void draw() {
        selectedCheck();
        Drawing.setStroke(2);
        Drawing.drawRect(Logic.gridToScreen(pos), new Vector2i(Math.round(getSize().x*zoomGridGap), Math.round(getSize().y*zoomGridGap))); 
    }
}
